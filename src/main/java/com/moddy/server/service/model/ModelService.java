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
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
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
import java.util.stream.Collectors;


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


        boolean applyStatus = hairModelApplicationJpaRepository.existsByUser(userId);
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


        List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByUser(userId);
        List<OfferResponse> offerResponseList = new ArrayList<>();

        for (int i = 0; i < hairServiceOffers.size(); i++){
            Designer designer = hairServiceOffers.get(i).getDesigner();
            List<PreferOfferCondition> preferOfferCondition = preferOfferConditionJpaRepository.findTop2ByHairServiceOffer(hairServiceOffers.get(i).getId());
            List<OfferCondition> offerConditionTop2List = preferOfferCondition.stream().map(PreferOfferCondition::getOfferCondition).collect(Collectors.toList());

            OfferResponse offerResponse = new OfferResponse(
                    hairServiceOffers.get(i).getId(),
                    designer.getProfileImgUrl(),
                    designer.getName(),
                    designer.getHairShop().getName(),
                    offerConditionTop2List,
                    hairServiceOffers.get(i).getIsClicked());

            offerResponseList.add(offerResponse);
        }

        return new ModelMainResponse(
                pageInfo,
                modelApplyStatus,
                user.getName(),
                offerResponseList
        );
    }

}
