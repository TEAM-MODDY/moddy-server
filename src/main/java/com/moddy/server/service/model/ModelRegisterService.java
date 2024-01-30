package com.moddy.server.service.model;

import com.moddy.server.domain.model.repository.ModelJpaRepository;
import com.moddy.server.domain.prefer_region.repository.PreferRegionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelRegisterService {
    private final PreferRegionJpaRepository preferRegionJpaRepository;
    private final ModelJpaRepository modelJpaRepository;

    public void deleteModelInfo(final Long modelId) {
        deleteModelPreferRegions(modelId);
        modelJpaRepository.deleteById(modelId);
    }

    private void deleteModelPreferRegions(final Long modelId) {
        preferRegionJpaRepository.deleteAllByModelId(modelId);
    }
}
