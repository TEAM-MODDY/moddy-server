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

import java.time.LocalDateTime;

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
    public Integer getAge(String year) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Integer age = currentDateTime.getYear() - Integer.parseInt(year) + 1;
        return age;
    }

    public void update( String name, Gender gender, String phoneNumber, Boolean isMarketingAgree, String profileImgUrl, Role role) {
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.isMarketingAgree = isMarketingAgree;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
    }

}
