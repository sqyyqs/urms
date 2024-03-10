package com.sqy.urms.core.service;

import com.sqy.urms.dto.requestentity.CreateRequestDto;
import com.sqy.urms.dto.requestentity.RequestDto;
import com.sqy.urms.dto.requestentity.SearchRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestStatusDto;
import com.sqy.urms.dto.util.AppUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestService {

    ResponseEntity<RequestDto> create(CreateRequestDto createRequestDto, String currentUsername);

    ResponseEntity<RequestDto> update(UpdateRequestDto updateRequestDto, String currentUsername);

    ResponseEntity<List<RequestDto>> search(SearchRequestDto searchRequestDto, AppUserDto appUserDto);

    ResponseEntity<RequestDto> findById(long id);

    ResponseEntity<RequestDto> updateStatus(UpdateRequestStatusDto updateRequestStatusDto, AppUserDto appUserDto);
}
