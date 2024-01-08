package com.moddy.server.domain.designer;

import com.moddy.server.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class Designer extends User {
    @NotNull
    private String hairShopAddress;
    @NotNull
    private String hairShopDetailAddress;
    @NotNull
    private String hairShopName;
    @NotNull
    private String instagramUrl;
    @NotNull
    private String naverPlaceUrl;
    @NotNull
    private String introduction;
    @NotNull
    private String kakaoOpenChatUrl;
}
