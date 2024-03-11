package com.sqy.urms.dadataclient;

import com.sqy.urms.dadataclient.dto.PhoneNumberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient(value = "dadataClient", url = "${dadata-client.url}")
public interface DadataClient {

    @PostMapping(value = "/api/v1/clean/phone", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Collection<PhoneNumberDto> getPhone(
            @RequestBody String phoneValue
    );

}
