package com.sqy.urms.core.service.impl;

import com.sqy.urms.core.mapper.RequestMapper;
import com.sqy.urms.core.service.RequestService;
import com.sqy.urms.dto.requestentity.CreateRequestDto;
import com.sqy.urms.dto.requestentity.RequestDto;
import com.sqy.urms.dto.requestentity.RequestStatus;
import com.sqy.urms.dto.requestentity.SearchRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestDto;
import com.sqy.urms.dto.requestentity.UpdateRequestStatusDto;
import com.sqy.urms.dto.util.AppUserDto;
import com.sqy.urms.dto.util.PhoneNumberUtils;
import com.sqy.urms.persistence.model.Request;
import com.sqy.urms.persistence.model.User;
import com.sqy.urms.persistence.repository.RequestRepository;
import com.sqy.urms.persistence.repository.UserRepository;
import com.sqy.urms.persistence.repository.specification.RequestSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.sqy.urms.dto.requestentity.RequestStatus.ACCEPTED;
import static com.sqy.urms.dto.requestentity.RequestStatus.DRAFT;
import static com.sqy.urms.dto.requestentity.RequestStatus.REJECTED;
import static com.sqy.urms.dto.requestentity.RequestStatus.SENT;
import static com.sqy.urms.persistence.model.UserRole.OPERATOR;
import static com.sqy.urms.persistence.model.UserRole.USER;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);
    private static final int ELEMENTS_ON_PAGE = 5;

    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestMapper requestMapper, RequestRepository requestRepository, UserRepository userRepository) {
        this.requestMapper = requestMapper;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<RequestDto> create(CreateRequestDto createRequestDto, String currentUsername) {
        logger.info("Invoke create({}, {}).", createRequestDto, currentUsername);

        PhoneNumberUtils.requireValidate(createRequestDto.phoneNumber());

        User user = userRepository.findByLogin(currentUsername);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Request request = requestMapper.fromCreateDto(createRequestDto, user.getId());
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        RequestDto dto = requestMapper.toDtoFetchUser(requestRepository.save(request), user);
        return ResponseEntity.ofNullable(dto);
    }


    @Override
    @Transactional
    public ResponseEntity<RequestDto> update(UpdateRequestDto updateRequestDto, String currentUsername) {
        logger.info("Invoke update({}).", updateRequestDto);

        PhoneNumberUtils.requireValidate(updateRequestDto.phoneNumber());

        Request request = requestRepository.findById(updateRequestDto.id()).orElse(null);
        if (request == null || request.getRequestStatus() != DRAFT || !request.getUser().getLogin().equals(currentUsername)) {
            return ResponseEntity.notFound().build();
        }
        request.setName(updateRequestDto.name());
        request.setPhoneNumber(updateRequestDto.phoneNumber());
        request.setText(updateRequestDto.text());

        RequestDto dto = requestMapper.toDto(requestRepository.save(request));
        return ResponseEntity.ofNullable(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<RequestDto>> search(SearchRequestDto searchRequestDto, AppUserDto appUserDto) {
        logger.info("Invoke search({}, {}).", searchRequestDto, appUserDto);
        if (!validateSearchRequest(searchRequestDto, appUserDto)) {
            return ResponseEntity.badRequest().build();
        }

        Page<Request> result = requestRepository.findAll(
                RequestSpecificationBuilder.search(searchRequestDto, appUserDto),
                PageRequest.of(searchRequestDto.page(), ELEMENTS_ON_PAGE)
        );

        return ResponseEntity.ok(result.stream().map(requestMapper::toDto).toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<RequestDto> findById(long id) {
        logger.info("Invoke findById({}).", id);
        return ResponseEntity.of(requestRepository.findById(id).map(requestMapper::toDto));
    }


    @Override
    @Transactional
    public ResponseEntity<RequestDto> updateStatus(UpdateRequestStatusDto updateRequestStatusDto, AppUserDto appUserDto) {
        logger.info("Invoke updateStatus({}, {}).", updateRequestStatusDto, appUserDto);

        RequestStatus newRequestStatus = updateRequestStatusDto.newRequestStatus();
        if (!validateUpdateRequestStatus(newRequestStatus, appUserDto.roles())) {
            return ResponseEntity.badRequest().build();
        }

        Request request = requestRepository.findById(updateRequestStatusDto.id()).orElse(null);
        if (request == null || (!request.getUser().getLogin().equals(appUserDto.currentUsername()) && newRequestStatus == SENT)) {
            return ResponseEntity.notFound().build();
        }

        if (newRequestStatus.getSequence() - request.getRequestStatus().getSequence() != 1) {
            return ResponseEntity.badRequest().build();
        }

        request.setRequestStatus(newRequestStatus);

        RequestDto dto = requestMapper.toDto(requestRepository.save(request));
        return ResponseEntity.ofNullable(dto);
    }

    private static boolean validateSearchRequest(SearchRequestDto searchRequestDto, AppUserDto appUserDto) {
        List<String> roles = appUserDto.roles();
        if (!StringUtils.hasText(appUserDto.currentUsername())
                || (searchRequestDto.fromCurrentUser() && !roles.contains(USER.getRoleName()))
                || (roles.contains(USER.getRoleName()) && roles.size() == 1 && !searchRequestDto.fromCurrentUser())) {
            return false;
        }
        return true;
    }

    private static boolean validateUpdateRequestStatus(RequestStatus newRequestStatus, List<String> roles) {
        if ((newRequestStatus == SENT && !roles.contains(USER.getRoleName()))
                || ((newRequestStatus == ACCEPTED || newRequestStatus == REJECTED) && !roles.contains(OPERATOR.getRoleName()))) {
            return false;
        }
        return true;
    }

}
