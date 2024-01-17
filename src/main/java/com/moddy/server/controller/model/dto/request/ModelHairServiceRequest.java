package com.moddy.server.controller.model.dto.request;

import com.moddy.server.domain.hair_service_record.ServiceRecord;
import com.moddy.server.domain.hair_service_record.ServiceRecordTerm;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record ModelHairServiceRequest(
        @Enumerated(EnumType.STRING)
        ServiceRecord hairService,
        @Enumerated(EnumType.STRING)
        ServiceRecordTerm hairServiceTerm
) {
}
