package com.moddy.server.domain.prefer_hair_style;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PreferHairStyle extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private HairModelApplication hairModelApplication;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private HairStyle hairStyle;

}
