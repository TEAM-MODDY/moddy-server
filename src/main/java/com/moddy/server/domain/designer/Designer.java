package com.moddy.server.domain.designer;

import com.moddy.server.controller.designer.dto.request.DesignerUpdateRequest;
import com.moddy.server.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Designer extends User {

    @Embedded
    private HairShop hairShop;

    @Embedded
    private Portfolio portfolio;

    @NotNull
    private String introduction;

    @NotNull
    private String kakaoOpenChatUrl;

    public void update(HairShop hairShop, Portfolio portfolio, String introduction, String kakaoOpenChatUrl){
        this.hairShop = hairShop;
        this.portfolio = portfolio;
        this.introduction = introduction;
        this.kakaoOpenChatUrl = kakaoOpenChatUrl;
    }
}
