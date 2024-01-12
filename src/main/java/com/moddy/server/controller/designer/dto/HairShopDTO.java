package com.moddy.server.controller.designer.dto;

import lombok.*;

@Builder
public record HairShopDTO(
        String name,
        String address,
        String detailAddress
)
{


}
