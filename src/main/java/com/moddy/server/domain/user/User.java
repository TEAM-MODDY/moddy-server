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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private Boolean isMarketingAgree;

    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void update(String name, Gender gender, String phoneNumber, Boolean isMarketingAgree, String profileImgUrl, Role role) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isMarketingAgree = isMarketingAgree;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }

    public String getSlashedPhoneNumber(String phoneNumber){
        return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
    }
}
