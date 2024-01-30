package com.moddy.server.service.application;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.HairModelApplicationResponse;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.service.designer.DesignerRetrieveService;
import com.moddy.server.service.model.ModelRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairModelApplicationRetrieveService {

    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final DesignerRetrieveService designerRetrieveService;
    private final ModelRetrieveService modelRetrieveService;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;

    public String getApplicationCaptureUrl(final Long applicationId){
        HairModelApplication application = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        return application.getApplicationCaptureUrl();
    }

    public DesignerMainResponse getDesignerMainInfo(final Long designerId, final int page, final int size) {

        Page<HairModelApplication> applicationPage = findApplicationsByPaging(page, size);
        long totalElements = applicationPage.getTotalElements();

        List<HairModelApplicationResponse> applicationResponsesList = applicationPage.stream().map(this::getApplicationResponse).collect(Collectors.toList());

        return new DesignerMainResponse(
                page,
                size,
                totalElements,
                designerRetrieveService.getDesignerName(designerId),
                applicationResponsesList
        );
    }
    private Page<HairModelApplication> findApplicationsByPaging(final int page, final int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findAll(pageRequest);

        return applicationPage;
    }

    private HairModelApplicationResponse getApplicationResponse(final HairModelApplication application) {
        Long modelId = application.getModel().getId();
        List<PreferHairStyle> preferHairStyle = preferHairStyleJpaRepository.findTop2ByHairModelApplicationId(application.getId());
        List<String> top2hairStyles = preferHairStyle.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());
        ApplicationModelInfoDto modelInfoDto = modelRetrieveService.getApplicationModelInfo(modelId);
        HairModelApplicationResponse applicationResponse = new HairModelApplicationResponse(
                application.getId(),
                modelInfoDto.name(),
                modelInfoDto.age(),
                application.getModelImgUrl(),
                modelInfoDto.gender(),
                top2hairStyles
        );
        return applicationResponse;
    }
}
