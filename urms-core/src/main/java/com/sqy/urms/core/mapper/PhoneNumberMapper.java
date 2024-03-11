package com.sqy.urms.core.mapper;

import com.sqy.urms.dadataclient.dto.PhoneNumberDto;
import com.sqy.urms.persistence.model.PhoneNumber;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class PhoneNumberMapper {

    @Nullable
    public PhoneNumber fromDto(@Nullable PhoneNumberDto phoneNumberDto) {
        if (phoneNumberDto == null) {
            return null;
        }

        return new PhoneNumber(phoneNumberDto.countryCode(), phoneNumberDto.cityCode(), phoneNumberDto.phone());
    }
}
