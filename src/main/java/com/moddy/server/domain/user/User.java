package com.moddy.server.domain.user;

import com.moddy.server.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    public Integer getAge(String year){
        LocalDateTime currentDateTime = LocalDateTime.now();
        Integer age = currentDateTime.getYear() - Integer.parseInt(year) + 1 ;
        return age;
    }
}
