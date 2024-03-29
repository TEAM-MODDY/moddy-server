package com.moddy.server.domain.day_off;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.designer.Designer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DayOff extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    @NotNull
    private Designer designer;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DayOfWeek dayOfWeek;


}
