package com.moddy.server.service.user;

import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.domain.user.User;
import com.moddy.server.domain.user.repository.UserRepository;
import com.moddy.server.service.application.HairModelApplicationRegisterService;
import com.moddy.server.service.designer.DesignerRegisterService;
import com.moddy.server.service.model.ModelRegisterService;
import com.moddy.server.service.offer.HairServiceOfferRegisterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.moddy.server.common.exception.enums.ErrorCode.USER_NOT_FOUND_EXCEPTION;
import static com.moddy.server.domain.user.Role.MODEL;

@Service
@RequiredArgsConstructor
public class UserRegisterService {
    private final UserRepository userRepository;
    private final HairServiceOfferRegisterService hairServiceOfferRegisterService;
    private final HairModelApplicationRegisterService hairModelApplicationRegisterService;
    private final ModelRegisterService modelRegisterService;
    private final DesignerRegisterService designerRegisterService;

    @Transactional
    public void withdraw(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION));
        if (user.getRole() == MODEL) deleteModelInfos(userId);
        else deleteDesignerInfos(user);
    }

    private void deleteModelInfos(final Long modelId) {
        hairServiceOfferRegisterService.deleteModelHairServiceOfferInfos(modelId);
        hairModelApplicationRegisterService.deleteModelApplications(modelId);
        modelRegisterService.deleteModelInfo(modelId);
        userRepository.deleteById(modelId);
    }

    private void deleteDesignerInfos(final User designer) {
        hairServiceOfferRegisterService.deleteDesignerHairServiceOfferInfos(designer.getId());
        designerRegisterService.deleteDesignerInfo(designer);
        userRepository.deleteById(designer.getId());
    }
}
