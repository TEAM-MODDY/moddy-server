package com.moddy.server.domain.designer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Embeddable
@Getter
public class HairShop {
    @NotNull
    @Column(name = "hair_shop_name")
    private String name;

    @NotNull
    @Column(name = "hair_shop_address")
    private String address;

    @NotNull
    @Column(name = "hair_shop_detail_address")
    private String detailAddress;
}
