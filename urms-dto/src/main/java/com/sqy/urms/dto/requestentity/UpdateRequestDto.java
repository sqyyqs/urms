package com.sqy.urms.dto.requestentity;

import com.sqy.urms.dto.util.PhoneNumberUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public record UpdateRequestDto (
        long id,
        @Nullable String text,
        String phoneNumber,
        String name
){
    public UpdateRequestDto(long id, @Nullable String text, String phoneNumber, String name) {
        this.id = id;
        this.text = text;
        this.phoneNumber = PhoneNumberUtils.requireValidate(phoneNumber);
        this.name = Objects.requireNonNull(name, "Name can't be null.");
    }
}
