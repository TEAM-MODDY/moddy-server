package com.moddy.server.domain.hair_model_application;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    public HairModelApplication(Model model, HairLength hairLength, String hairDetail, String modelImgUrl, String instagramId, String applicationCaptureUrl) {
        this.model = model;
        this.hairLength = hairLength;
        this.hairDetail = hairDetail;
        this.modelImgUrl = modelImgUrl;
        this.instagramId = instagramId;
        this.applicationCaptureUrl = applicationCaptureUrl;
    }

    public LocalDate getCreateDate(){
        LocalDate createDate = getCreatedAt().toLocalDate();
        return createDate;
    }
    public LocalDate getExpireDate(){
        LocalDate expiredDate = getCreatedAt().plusDays(7).toLocalDate();
        return expiredDate;
    }
}
