package com.moddy.server.service.application;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.application.dto.response.ApplicationIdResponse;
import com.moddy.server.controller.application.dto.response.ApplicationInfoDetailResponse;
import com.moddy.server.controller.designer.dto.response.DesignerMainResponse;
import com.moddy.server.controller.designer.dto.response.DownloadUrlResponseDto;
import com.moddy.server.controller.designer.dto.response.HairModelApplicationResponse;
import com.moddy.server.controller.designer.dto.response.HairRecordResponse;
import com.moddy.server.controller.model.dto.ApplicationDto;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.controller.model.dto.response.ApplicationImgUrlResponse;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import com.moddy.server.domain.hair_service_record.HairServiceRecord;
import com.moddy.server.domain.hair_service_record.repository.HairServiceRecordJpaRepository;
import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;
import com.moddy.server.domain.prefer_hair_style.repository.PreferHairStyleJpaRepository;
import com.moddy.server.external.s3.S3Service;
import com.moddy.server.service.designer.DesignerRetrieveService;
import com.moddy.server.service.model.ModelRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairModelApplicationRetrieveService {

    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;
    private final DesignerRetrieveService designerRetrieveService;
    private final ModelRetrieveService modelRetrieveService;
    private final S3Service s3Service;
    private final PreferHairStyleJpaRepository preferHairStyleJpaRepository;
    private final HairServiceRecordJpaRepository hairServiceRecordJpaRepository;
    private static final String DATE_FORMAT = "yyyy. MM. dd.";

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

    public ApplicationInfoDetailResponse getOfferApplicationInfo(final Long applicationId) {
        List<String> preferHairStyleList = getPreferHairStyle(applicationId);
        String applicationHairDetail = getApplicationHairDetail(applicationId);

        return new ApplicationInfoDetailResponse(
                applicationId,
                preferHairStyleList,
                applicationHairDetail
        );
    }

    public ApplicationDto getApplicationDetailInfo(final Long applicationId) {
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
                hairModelApplication.getInstagramId(),
                hairModelApplication.getCreatedDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                hairModelApplication.getExpiredDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }

    public ApplicationIdResponse checkValidApplicationStatus(final Long modelId) {
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findFirstByModelIdOrderByCreatedAtDesc(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));

        if (hairModelApplication.isExpired())
            throw new NotFoundException(ErrorCode.NOT_FOUND_VALID_APPLICATION_EXCEPTION);

        return new ApplicationIdResponse(hairModelApplication.getId());
    }

    public boolean fetchModelApplyStatus(final Long modelId) {
        return hairModelApplicationJpaRepository.existsByModelId(modelId);
    }

    public ApplicationImgUrlResponse getApplicationImgUrl(final Long applicationId) {

        return new ApplicationImgUrlResponse(hairModelApplicationJpaRepository.findById(applicationId).get().getApplicationCaptureUrl());
    }

    public DownloadUrlResponseDto getApplicationCaptureDownloadUrl(final Long applicationId) {
        final HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        final String applicationDownloadUrl = s3Service.getPreSignedUrlToDownload(hairModelApplication.getApplicationCaptureUrl());
        return new DownloadUrlResponseDto(applicationDownloadUrl);
    }

    public boolean getApplicationExpiredStatus(final Long applicationId) {
        final HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        return hairModelApplication.isExpired();
    }

    private Page<HairModelApplication> findApplicationsByPaging(final int page, final int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<HairModelApplication> applicationPage = hairModelApplicationJpaRepository.findAll(pageRequest);

        long nonExpiredCount = hairModelApplicationJpaRepository.findAll()
                    .stream()
                    .filter(application -> !application.isExpired())
                    .count();

        Page<HairModelApplication> nonExpiredApplications = applicationPage
                .stream()
                .filter(application -> !application.isExpired())
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> new PageImpl<>(list, pageRequest, nonExpiredCount)  // 전체 개수 사용
                ));

        return nonExpiredApplications;
    }

    private String getApplicationHairDetail(final Long applicationId) {
        HairModelApplication hairModelApplication = hairModelApplicationJpaRepository.findById(applicationId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_APPLICATION_EXCEPTION));
        return hairModelApplication.getHairDetail();
    }

    private List<String> getPreferHairStyle(final Long applicationId) {
        List<PreferHairStyle> preferHairStyles = preferHairStyleJpaRepository.findAllByHairModelApplicationId(applicationId);
        List<String> preferHairStyleList = preferHairStyles.stream().map(hairStyle -> {
            return hairStyle.getHairStyle().getValue();
        }).collect(Collectors.toList());

        return preferHairStyleList;
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

