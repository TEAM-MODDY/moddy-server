package com.moddy.server.domain.designer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Portfolio {
    @NotNull
    private String instagramUrl;

    @NotNull
    private String naverPlaceUrl;

}
