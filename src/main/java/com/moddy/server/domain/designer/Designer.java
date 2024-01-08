package com.moddy.server.domain.designer;

import com.moddy.server.domain.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Designer extends User {

    @Embedded
    private HairShop hairShop;

    @Embedded
    private Portfolio portfolio;

    @NotNull
    private String introduction;

    @NotNull
    private String kakaoOpenChatUrl;

}
