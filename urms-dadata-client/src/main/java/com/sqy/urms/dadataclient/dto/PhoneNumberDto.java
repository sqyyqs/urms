package com.sqy.urms.dadataclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record PhoneNumberDto(
        String countryCode,
        String cityCode,
        String phone
) {

    public PhoneNumberDto(@JsonProperty("country_code") String countryCode,
                          @JsonProperty("city_code") String cityCode,
                          @JsonProperty("phone") String phone) {
        this.countryCode = Objects.requireNonNull(countryCode, "Country code can't be null.");
        this.cityCode = Objects.requireNonNull(cityCode, "City code can't be null.");
        this.phone = Objects.requireNonNull(phone, "Phone can't be null.");
    }
}
