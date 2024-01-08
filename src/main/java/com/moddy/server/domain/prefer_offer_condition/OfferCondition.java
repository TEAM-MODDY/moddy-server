package com.moddy.server.domain.prefer_offer_condition;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OfferCondition {
    CAMERA("얼굴 촬영"), MASK("마스크 착용"), SNS("SNS 게시"), PHOTOSHOP("포토샵 보정"), FREE("전액 무료"), SMALL_PAY("소정의 약값");

    private final String value;
}
