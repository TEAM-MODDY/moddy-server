package com.moddy.server.service.application;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.HairModelApplicationResponse;
import com.moddy.server.controller.designer.dto.response.HairRecordResponse;
import com.moddy.server.controller.model.dto.ApplicationDto;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
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

import java.util.Comparator;
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
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;

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
    public ApplicationDto getApplicationDetailInfo(final Long applicationId){
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        Long modelId = hairModelApplication.getModel().getId();
        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(applicationId);
        List<String> preferhairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        List<HairServiceRecord> hairServiceRecords = hairServiceRecordJpaRepository.findAllByHairModelApplicationId(applicationId);
        hairServiceRecords.sort(Comparator.comparingInt(e -> e.getServiceRecordTerm().ordinal()));

        List<HairRecordResponse> recordResponseList = hairServiceRecords.stream().map(records -> {
            HairRecordResponse hairRecordResponse = new HairRecordResponse(
                    records.getServiceRecordTerm().getValue(),
                    records.getServiceRecord().getValue()
            );
            return hairRecordResponse;
        }).collect(Collectors.toList());

        return new ApplicationDto(
                modelId,
                hairModelApplication.getModelImgUrl(),
                hairModelApplication.getHairLength().getValue(),
                preferhairStyleList,
                recordResponseList,
                hairModelApplication.getHairDetail(),
                hairModelApplication.getInstagramId());
    }
    public String fetchApplicationHairDetail(final Long applicationId) {
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        return hairModelApplication.getHairDetail();
    }

    public List<String> fetchPreferHairStyle(final Long applicationId) {
        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(applicationId);
        List<String> preferHairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        return preferHairStyleList;
    }


    public boolean fetchModelApplyStatus(final Long modelId){
        return hairModelApplicationJpaRepository.existsByModelId(modelId);
    }

    private Page<HairModelApplication> findApplicationsByPaging(final int page, final int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findAll(pageRequest);

        return applicationPage;
    }

    private HairModelApplicationResponse getApplicationResponse(final HairModelApplication application) {
        List<PreferHairStyle> preferHairStyle = preferHairStyleJpaRepository.findTop2ByHairModelApplicationId(application.getId());
        List<String> top2hairStyles = preferHairStyle.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());
        Long modelId = application.getModel().getId();
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

