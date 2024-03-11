package com.sqy.urms.dto.requestentity;

import com.sqy.urms.dto.user.UserDto;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;

public record RequestDto(
        long id,
        RequestStatus requestStatus,
        @Nullable String text,
        String phoneNumber,
        String name,
        Date createdAt,
        UserDto fromUser
) {
    public RequestDto(long id, RequestStatus requestStatus, @Nullable String text, String phoneNumber, String name, Date createdAt, UserDto fromUser) {
        this.id = id;
        this.requestStatus = requestStatus;
        this.text = text;
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number can't be null");
        this.name = Objects.requireNonNull(name, "Name can't be null.");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation date can't be null.");
        this.fromUser = Objects.requireNonNull(fromUser, "Author can't be null");
    }
}
