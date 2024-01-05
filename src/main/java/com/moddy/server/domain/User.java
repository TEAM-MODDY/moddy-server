package com.moddy.server.domain;


import com.moddy.server.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, name = "kakao_id")
    private Long socialId;

    public User(Long socialId) {

        this.socialId = socialId;
    }

    public static User of(Long socialId) {
        return new User(socialId);
    }
}
