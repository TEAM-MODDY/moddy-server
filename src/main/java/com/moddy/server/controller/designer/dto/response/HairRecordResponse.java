package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.hair_service_record.ServiceRecord;

public record HairRecordResponse(
        ServiceRecord hairServiceTerm,
        com.moddy.server.domain.hair_service_record.@jakarta.validation.constraints.NotNull ServiceRecordTerm hairService
) {
}
