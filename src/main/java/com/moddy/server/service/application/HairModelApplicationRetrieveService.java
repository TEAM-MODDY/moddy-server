package com.moddy.server.service.application;

import com.moddy.server.domain.hair_model_application.repository.HairModelApplicationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HairModelApplicationRetrieveService {
    private final HairModelApplicationJpaRepository hairModelApplicationJpaRepository;

}
