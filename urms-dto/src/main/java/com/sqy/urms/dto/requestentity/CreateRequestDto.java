package com.sqy.urms.dto.requestentity;

import com.sqy.urms.dto.util.PhoneNumberUtils;

import javax.annotation.Nullable;
import java.util.Objects;

public record CreateRequestDto(
        @Nullable String text,
        String phoneNumber,
        String name
) {

    public CreateRequestDto(@Nullable String text, String phoneNumber, String name) {
        this.text = text;
        this.phoneNumber = PhoneNumberUtils.requireValidate(phoneNumber);
        this.name = Objects.requireNonNull(name, "Name can't be null.");
    }
}
