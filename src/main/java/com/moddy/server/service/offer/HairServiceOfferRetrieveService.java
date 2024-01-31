package com.moddy.server.service.offer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.DesignerInfoOpenChatDto;
import com.moddy.server.controller.model.dto.response.DesignerInfoOpenChatResponse;
import com.moddy.server.controller.model.dto.response.DesignerInfoResponse;
import com.moddy.server.controller.model.dto.response.OpenChatResponse;
import com.moddy.server.controller.model.dto.response.StyleDetailResponse;
import com.moddy.server.controller.offer.response.DetailOfferResponse;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import com.moddy.server.service.application.HairModelApplicationRetrieveService;
import com.moddy.server.service.designer.DesignerRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairServiceOfferRetrieveService {

    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final DesignerRetrieveService designerRetrieveService;
    private final HairModelApplicationRetrieveService hairModelApplicationRetrieveService;

    public OpenChatResponse getOpenChatInfo(final Long userId, final Long offerId) {
        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUNT_OFFER_EXCEPTION));

        Long designerId = hairServiceOffer.getDesigner().getId();
        Long applicationId = hairServiceOffer.getHairModelApplication().getId();

        DesignerInfoOpenChatDto openChatDto = designerRetrieveService.getDesignerOpenDetail(designerId);

        DesignerInfoOpenChatResponse response = new DesignerInfoOpenChatResponse(openChatDto.imgUrl(), openChatDto.shopName(), openChatDto.name(), openChatDto.introduction());

        OpenChatResponse openChatResponse = new OpenChatResponse(hairModelApplicationRetrieveService.getApplicationCaptureUrl(applicationId), openChatDto.kakaoUrl(), response);

        return openChatResponse;
    }

    public DetailOfferResponse getOfferDetail(final Long modelId, final Long offerId) {

        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        DesignerInfoResponse designerInfoResponse = designerRetrieveService.getOfferDesignerInfoResponse(hairServiceOffer.getDesigner().getId());
        StyleDetailResponse styleDetailResponse = getStyleDetailResponse(hairServiceOffer, offerId);
        handleOfferClickStatus(hairServiceOffer);

        return new DetailOfferResponse(designerInfoResponse, styleDetailResponse);
    }

    private StyleDetailResponse getStyleDetailResponse(final HairServiceOffer hairServiceOffer, final Long offerId) {

        String applicationHairDetail = hairModelApplicationRetrieveService.fetchApplicationHairDetail(hairServiceOffer.getHairModelApplication().getId());
        List<String> preferHairStyleList = hairModelApplicationRetrieveService.fetchPreferHairStyle(hairServiceOffer.getHairModelApplication().getId());

        List<PreferOfferCondition> preferOfferConditionList = preferOfferConditionJpaRepository.findAllByHairServiceOfferId(offerId);
        List<OfferCondition> offerConditionList = preferOfferConditionList.stream().map(PreferOfferCondition::getOfferCondition).collect(Collectors.toList());
        List<Boolean> preferOfferConditionBooleanList = Arrays.stream(OfferCondition.values()).map(condition -> {
            if (offerConditionList.contains(condition)) return true;
            else return false;
        }).collect(Collectors.toList());

        StyleDetailResponse styleDetailResponse = new StyleDetailResponse(hairServiceOffer.getIsModelAgree(), preferHairStyleList, hairServiceOffer.getOfferDetail(), applicationHairDetail, preferOfferConditionBooleanList);

        return styleDetailResponse;
    }

    private void handleOfferClickStatus(final HairServiceOffer hairServiceOffer) {
        if (!hairServiceOffer.getIsClicked()) {
            hairServiceOffer.updateClickStatus();
        }
    }
}
