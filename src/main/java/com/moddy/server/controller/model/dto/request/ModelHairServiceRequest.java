package com.moddy.server.controller.model.dto.request;

import com.moddy.server.domain.hair_service_record.ServiceRecord;
import com.moddy.server.domain.hair_service_record.ServiceRecordTerm;

public record ModelHairServiceRequest(
        ServiceRecord hairService,
        ServiceRecordTerm hairServiceTerm
) {
}
