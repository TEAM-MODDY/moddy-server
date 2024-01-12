package com.moddy.server.controller.designer.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HairShopDTO {

    private String name;

    private String address;

    private String detailAddress;
}
