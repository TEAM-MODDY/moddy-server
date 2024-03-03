package com.moddy.server.service.offer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.ConflictException;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.common.util.SmsUtil;
import com.moddy.server.controller.designer.dto.request.OfferCreateRequest;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.moddy.server.common.exception.enums.ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class HairServiceOfferRegisterService {
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;
    private final DesignerJpaRepository designerJpaRepository;
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final SmsUtil smsUtil;

    @Transactional
    public void deleteModelHairServiceOfferInfos(final Long modelId) {
        final List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByModelId(modelId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }

    public void deleteDesignerHairServiceOfferInfos(final Long designerId) {
        final List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByDesignerId(designerId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }

    @Transactional
    public void postOffer(final Long designerId, final Long applicationId, final OfferCreateRequest request) throws IOException {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND_EXCEPTION));
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        Model model = hairModelApplication.getModel();
        if (hairServiceOfferJpaRepository.existsByHairModelApplicationIdAndDesignerId(applicationId,designer.getId())) throw new ConflictException(ErrorCode.ALREADY_EXIST_OFFER_EXCEPTION);

        HairServiceOffer offer = new HairServiceOffer(hairModelApplication,model,designer, request.offerDetail(), false,false);
        hairServiceOfferJpaRepository.save(offer);

        request.preferOfferConditions().stream()
                .forEach(p -> {
                    PreferOfferCondition preferOfferCondition = new PreferOfferCondition(offer,p);
                    preferOfferConditionJpaRepository.save(preferOfferCondition);

                });

        final String modelName = model.getName();
        final String modelPhoneNumber = model.getPhoneNumber();
        sendSms(smsUtil, modelPhoneNumber,modelName);
    }

    public void updateOfferAgreeStatus(final Long offerId) {
        HairServiceOffer hairServiceOffer = hairServiceOfferJpaRepository.findById(offerId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_OFFER_EXCEPTION));

        hairServiceOffer.agreeOfferToModel();
    }

    private void sendSms(final SmsUtil smsUtil, final String modelPhoneNumber, final String modelName) throws IOException {
        smsUtil.sendOfferToModel(modelPhoneNumber, modelName);
    }

}
