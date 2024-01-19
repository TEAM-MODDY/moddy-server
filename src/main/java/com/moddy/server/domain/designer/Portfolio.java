package com.moddy.server.domain.designer;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Portfolio {
    @NotNull
    private String instagramUrl;

    @NotNull
    private String naverPlaceUrl;

}
