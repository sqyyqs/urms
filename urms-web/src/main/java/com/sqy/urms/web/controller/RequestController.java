package com.sqy.urms.web.controller;

import com.sqy.urms.core.service.RequestService;
import com.sqy.urms.dto.requestentity.CreateRequestDto;
import com.sqy.urms.dto.requestentity.RequestDto;
import com.sqy.urms.dto.requestentity.SearchRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestStatusDto;
import com.sqy.urms.web.controller.mapper.UserDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/request")
public class RequestController {

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    private final RequestService requestService;
    private final UserDetailsMapper userDetailsMapper;

    public RequestController(RequestService requestService, UserDetailsMapper userDetailsMapper) {
        this.requestService = requestService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<RequestDto> createRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody CreateRequestDto createRequestDto) {
        logger.info("Invoke createRequest({}).", createRequestDto);
        return requestService.create(createRequestDto, userDetails.getUsername());
    }

    @PutMapping("/update")
    public ResponseEntity<RequestDto> updateRequest(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody UpdateRequestDto updateRequestDto) {
        logger.info("Invoke updateRequest({}).", updateRequestDto);
        return requestService.update(updateRequestDto, userDetails.getUsername());
    }

    @PostMapping("/search")
    public ResponseEntity<List<RequestDto>> search(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody SearchRequestDto searchRequestDto) {
        logger.info("Invoke search({}).", searchRequestDto);
        return requestService.search(searchRequestDto, userDetailsMapper.toDto(userDetails));
    }


    @PutMapping("/updateStatus")
    public ResponseEntity<RequestDto> updateRequestStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody UpdateRequestStatusDto updateRequestStatusDto) {
        logger.info("Invoke updateRequestStatus({}).", updateRequestStatusDto);
        return requestService.updateStatus(updateRequestStatusDto, userDetailsMapper.toDto(userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDto> findById(@PathVariable long id) {
        logger.info("Invoke findById({}).", id);
        return requestService.findById(id);
    }


}
