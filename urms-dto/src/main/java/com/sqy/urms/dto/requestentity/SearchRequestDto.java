package com.sqy.urms.dto.requestentity;

import com.sqy.urms.dto.util.OrderType;

import javax.annotation.Nullable;

public record SearchRequestDto(
        @Nullable String name,
        OrderType dateOrderType,
        int page,
        boolean fromCurrentUser
) {

}
