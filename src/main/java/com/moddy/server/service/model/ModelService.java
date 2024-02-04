package com.moddy.server.service.model;


import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.response.ApplicationUserDetailResponse;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelJpaRepository modelJpaRepository;
    private final PreferRegionJpaRepository preferRegionJpaRepository;

    public ApplicationUserDetailResponse getUserDetailInApplication(final Long userId) {
        Model model = modelJpaRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        List<String> preferRegions = preferRegionJpaRepository.findAllByModelId(model.getId())
                .stream()
                .map(p -> p.getRegion().getName())
                .toList();

        return new ApplicationUserDetailResponse(model.getName(), model.getGender().getValue(), model.getAge(), preferRegions);
    }

}
