package com.moddy.server.domain.designer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HairShop {

    @Column(name = "hair_shop_name")
    @NotNull
    private String name;


    @Column(name = "hair_shop_address")
    @NotNull
    private String address;

    @Column(name = "hair_shop_detail_address")
    @NotNull
    private String detailAddress;
}
