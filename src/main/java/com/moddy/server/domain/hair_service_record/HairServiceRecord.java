package com.moddy.server.domain.hair_service_record;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.hair_model_application.HairModelApplication;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
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


}
