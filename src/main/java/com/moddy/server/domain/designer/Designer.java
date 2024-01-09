package com.moddy.server.domain.designer;

import com.moddy.server.domain.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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



}
