package com.moddy.server.domain.user;

import com.moddy.server.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String kakaoId;

    @NotNull
    private String name;

    @NotNull
    private String year;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Integer isMarketingAgree;

    @NotNull
    private String profileImgUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String kakaoId, String name, String year, Gender gender, String phoneNumber,  Integer isMarketingAgree, String profileImgUrl ) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.year = year;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isMarketingAgree = isMarketingAgree;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }

}
