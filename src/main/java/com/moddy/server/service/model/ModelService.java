package com.moddy.server.service.model;


import com.moddy.server.common.dto.PageInfo;
import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.response.ModelMainResponse;
import com.moddy.server.controller.model.dto.response.OfferResponse;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.har_service_offer.HairServiceOffer;
import com.moddy.server.domain.har_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.model.ModelApplyStatus;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelJpaRepository modelJpaRepository;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;

    private Page<HairServiceOffer> findOffers(int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<HairServiceOffer> offerPage = hairServiceOfferJpaRepository.findAllByOrderByHairServiceOfferIdDesc(pageRequest);

        return offerPage;
    }

    private ModelApplyStatus calModelStatus(boolean apply, boolean offer){

        //테스트 코드에 false true 경우 있는지 테스트하기
        if(apply == false && offer == false) {
            return ModelApplyStatus.NOTHING;
        }
        else if (apply == true && offer == false) {
            return ModelApplyStatus.APPLY;
        }
        else if (apply == true && offer == true) {
            return ModelApplyStatus.APPLY_AND_OFFER;
        }
        else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_MODEL_STATUS);
        }

    }

    public ModelMainResponse getModelMainInfo(Long userId, int page, int size){

        Page<HairServiceOffer> offerPage = findOffers(page,size);
        PageInfo pageInfo = new PageInfo(page, size);


        //STATUS 계산해야함 -> 그럴려면 해당 유저의 userId를 알아야함, 그리고 그 유저 아이디로 Application 조회
        boolean applyStatus = hairModelApplicationJpaRepository.existsByUser(userId);

        //STATUS 계산해야함 -> 그럴려면 해당 유저의 userId를 알아야함, 그리고 그 유저 아이디로 Offer 조회
        boolean offerStatus = hairServiceOfferJpaRepository.existsByUser(userId);

        ModelApplyStatus modelApplyStatus = calModelStatus(applyStatus, offerStatus);
        User user = modelJpaRepository.findByUser(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));


        if(modelApplyStatus != ModelApplyStatus.APPLY_AND_OFFER){
            return new ModelMainResponse(
                    pageInfo,
                    modelApplyStatus,
                    user.getName(),
                    new ArrayList<>()
            );
        }

        //offers 조회
        List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByUser(userId);

        List<OfferResponse> offerResponseList = new ArrayList<>();
        //preferOfferConditionJpaRepository

        for (int i = 0; i < hairServiceOffers.size(); i++){
            Designer designer = hairServiceOffers.get(i).getDesigner();
            PreferOfferCondition preferOfferCondition = preferOfferConditionJpaRepository.findByHairServiceOffer(hairServiceOffers.get(i).getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PREFER_OFFER_CONDITION));;
            offerResponseList.add(
                    i,
                    hairServiceOffers.get(i).getId(),
                    designer.getProfileImgUrl(),
                    designer.getName(),
                    designer.getHairShop().getName(),


            );

        }

        //offers
//        "offerId" : 1
//        "imgUrl" : "http://디자이너이미지겠죠?",
//                "name" : "백모디1",
//                "shopName" : "모디 헤어1",
//                "conditions" : [
//        "전액 무료",
//                "소정의 재료비"
//								],
//        "isClicked" : true
        //isClicked  로직은 상세보기 했을 떄 구현하는 걸로


        return new ModelMainResponse(
                pageInfo,
                modelApplyStatus,
                user.getName(),

        );
    }

}
