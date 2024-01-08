package com.moddy.server.domain.user;

import com.moddy.server.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
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
    private String isMarketingAgree;

    @NotNull
    private String profileImgUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
