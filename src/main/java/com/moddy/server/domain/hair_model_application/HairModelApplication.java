package com.moddy.server.domain.hair_model_application;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.model.Model;
import com.moddy.server.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HairModelApplication extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @NotNull
    private Model model;

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

}
