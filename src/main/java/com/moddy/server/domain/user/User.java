package com.moddy.server.domain.user;

import com.moddy.server.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String kakaoId;

    @NotNull
    private String name;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Boolean isMarketingAgree;

    @NotNull
    private String profileImgUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String kakaoId, String name, Gender gender, String phoneNumber,  Boolean isMarketingAgree, String profileImgUrl ) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isMarketingAgree = isMarketingAgree;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }

}
