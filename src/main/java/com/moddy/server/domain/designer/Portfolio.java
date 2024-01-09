package com.moddy.server.domain.designer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Embeddable
@Getter
public class Portfolio {
    @NotNull
    private String instagramUrl;

    @NotNull
    private String naverPlaceUrl;

}
