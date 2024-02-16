package com.moddy.server.service.model;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.auth.dto.response.RegionResponse;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.ModelApplyStatus;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.region.repository.RegionJpaRepository;
import com.moddy.server.service.offer.HairServiceOfferRetrieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelRetrieveService {
    private final ModelJpaRepository modelJpaRepository;
    private final RegionJpaRepository regionJpaRepository;

    public ApplicationModelInfoDto getApplicationModelInfo(final Long modelId) {
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        return new ApplicationModelInfoDto(model.getName(), model.getAge(), model.getGender().getValue());
    }

    public List<RegionResponse> getRegionList() {
        List<RegionResponse> regionResponseList = regionJpaRepository.findAll().stream().map(region -> {
            RegionResponse regionResponse = new RegionResponse(region.getId(), region.getName());
            return regionResponse;
        }).collect(Collectors.toList());

        return regionResponseList;
    }

    public String getModelName(final Long modelId) {
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        return model.getName();
    }
}
