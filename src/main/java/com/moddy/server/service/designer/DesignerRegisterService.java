package com.moddy.server.service.designer;

import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import com.moddy.server.domain.user.User;
import com.moddy.server.external.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DesignerRegisterService {
    private final DesignerJpaRepository designerJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;
    private final S3Service s3Service;

    public void deleteDesignerInfo(final User designer) {
        dayOffJpaRepository.deleteAllByDesignerId(designer.getId());
        s3Service.deleteS3Image(designer.getProfileImgUrl());
        designerJpaRepository.deleteById(designer.getId());
    }
}
