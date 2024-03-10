package com.sqy.urms.dto.util;

import com.sqy.urms.dto.exception.PhoneValidationException;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PhoneNumberUtils {
    private PhoneNumberUtils() {
        throw new RuntimeException();
    }

    public static String requireValidate(@Nullable String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new PhoneValidationException("Phone number is empty.");
        }
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new PhoneValidationException("Phone number is not 10 numbers.");
        }

        return phoneNumber;
    }

}
