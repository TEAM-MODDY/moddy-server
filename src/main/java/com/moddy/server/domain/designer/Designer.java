package com.moddy.server.domain.designer;

import com.moddy.server.domain.user.Gender;
import com.moddy.server.domain.user.Role;
import com.moddy.server.domain.user.User;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Designer extends User {

    @Embedded
    private HairShop hairShop;

    @Embedded
    private Portfolio portfolio;

    private String introduction;

    private String kakaoOpenChatUrl;
}
