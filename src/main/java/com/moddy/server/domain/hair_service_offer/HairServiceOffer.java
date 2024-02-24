package com.moddy.server.domain.hair_service_offer;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.designer.Designer;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

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

    private boolean isModelAgree;

    private boolean isClicked;

    public HairServiceOffer(HairModelApplication hairModelApplication, Model model, Designer designer, String offerDetail, boolean isModelAgree, boolean isClicked) {
        this.hairModelApplication = hairModelApplication;
        this.model = model;
        this.designer = designer;
        this.offerDetail = offerDetail;
        this.isModelAgree = isModelAgree;
        this.isClicked = isClicked;
    }

    public void agreeOfferToModel(){
        this.isModelAgree = true;
    }

    public void updateClickStatus() { this.isClicked = true; }


}
