package com.sqy.urms.dto.requestentity;

public record UpdateRequestStatusDto(
        long id,
        RequestStatus newRequestStatus
) {
}
