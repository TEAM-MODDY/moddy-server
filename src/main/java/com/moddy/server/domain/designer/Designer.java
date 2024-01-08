package com.moddy.server.domain.designer;

import com.moddy.server.domain.user.Gender;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
