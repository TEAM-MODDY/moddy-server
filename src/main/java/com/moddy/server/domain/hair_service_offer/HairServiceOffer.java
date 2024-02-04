package com.moddy.server.domain.hair_service_offer;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HairServiceOffer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private HairModelApplication hairModelApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @NotNull
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_id")
    @NotNull
    private Designer designer;

    @NotNull
    private String offerDetail;

    @NotNull
    private Boolean isModelAgree;

    @NotNull
    private Boolean isClicked;

    public void agreeOfferToModel(){
        this.isModelAgree = true;
    }

    public void updateClickStatus() { this.isClicked = true; }

}
