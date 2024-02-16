package com.moddy.server.domain.hair_service_record;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
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
public class HairServiceRecord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private HairModelApplication hairModelApplication;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private ServiceRecord serviceRecord;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private ServiceRecordTerm serviceRecordTerm;

    public HairServiceRecord(HairModelApplication hairModelApplication, ServiceRecord serviceRecord, ServiceRecordTerm serviceRecordTerm) {
        this.hairModelApplication = hairModelApplication;
        this.serviceRecord = serviceRecord;
        this.serviceRecordTerm = serviceRecordTerm;
    }
}
