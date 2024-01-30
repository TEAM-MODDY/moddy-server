package com.moddy.server.service.model;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.ApplicationModelInfoDto;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.model.repository.ModelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelRetrieveService {
    private final ModelJpaRepository modelJpaRepository;

    public ApplicationModelInfoDto getApplicationModelInfo(final Long modelId){
        Model model = modelJpaRepository.findById(modelId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MODEL_INFO));
        return new ApplicationModelInfoDto(model.getName(),model.getAge(), model.getGender().getValue());
    }
}
