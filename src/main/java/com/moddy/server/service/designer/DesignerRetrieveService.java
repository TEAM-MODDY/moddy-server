package com.moddy.server.service.designer;

import com.moddy.server.common.exception.enums.ErrorCode;
import com.moddy.server.common.exception.model.NotFoundException;
import com.moddy.server.controller.model.dto.DesignerInfoOpenChatDto;
import com.moddy.server.controller.model.dto.response.DesignerInfoResponse;
import com.moddy.server.domain.day_off.DayOff;
import com.moddy.server.domain.day_off.repository.DayOffJpaRepository;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.designer.repository.DesignerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesignerRetrieveService {

    private final DesignerJpaRepository designerJpaRepository;
    private final DayOffJpaRepository dayOffJpaRepository;

    public String getDesignerName(final Long designerId) {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));
        return designer.getName();
    }

    public DesignerInfoOpenChatDto getDesignerOpenDetail(final Long designerId) {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));
        return new DesignerInfoOpenChatDto(designer.getKakaoOpenChatUrl(), designer.getProfileImgUrl(), designer.getHairShop().getName(), designer.getName(), designer.getIntroduction());
    }

    public DesignerInfoResponse getOfferDesignerInfoResponse(final Long designerId) {
        Designer designer = designerJpaRepository.findById(designerId).orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND_EXCEPTION));
        List<String> dayOfWeekList = getDayOfWeekList(designerId);

        DesignerInfoResponse designerInfoResponse = new DesignerInfoResponse(designerId, designer.getProfileImgUrl(), designer.getHairShop().getName(), designer.getName(), designer.getPortfolio().getInstagramUrl(), designer.getPortfolio().getNaverPlaceUrl(), designer.getIntroduction(), designer.getGender().getValue(), dayOfWeekList, designer.getHairShop().getAddress(), designer.getHairShop().getDetailAddress());
        return designerInfoResponse;
    }

    private List<String> getDayOfWeekList(final Long designerId) {
        List<DayOff> dayOffList = dayOffJpaRepository.findAllByDesignerId(designerId);
        List<String> dayOfWeekList = dayOffList.stream().map(dayOff -> {
            return dayOff.getDayOfWeek().getValue();
        }).collect(Collectors.toList());

        return dayOfWeekList;
    }
}
