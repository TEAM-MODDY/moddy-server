package com.moddy.server.domain.hair_model_application;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
public class HairModelApplication extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id_")
    @NotNull
    private Designer designer;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private HairLength hairLength;

    @NotNull
    private String hairDetail;

    @NotNull
    private String modelImgUrl;

    @NotNull
    private String instagramId;

    @NotNull
    private String applicationCaptureUrl;

    @NotNull
    private Boolean isSend;

}
