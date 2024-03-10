package com.sqy.urms.core.mapper;

import com.sqy.urms.dto.requestentity.CreateRequestDto;
import com.sqy.urms.dto.requestentity.RequestDto;
import com.sqy.urms.dto.requestentity.RequestStatus;
import com.sqy.urms.persistence.model.Request;
import com.sqy.urms.persistence.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class RequestMapper {

    private final UserMapper userMapper;

    public RequestMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Nullable
    public Request fromCreateDto(@Nullable CreateRequestDto createRequestDto, @Nullable Long userId) {
        if (createRequestDto == null || userId == null) {
            return null;
        }

        User requestAuthor = new User();
        requestAuthor.setId(userId);
        return new Request(
                null,
                RequestStatus.DRAFT,
                createRequestDto.text(),
                createRequestDto.phoneNumber(),
                createRequestDto.name(),
                null,
                requestAuthor
        );
    }

    @Nullable
    public RequestDto toDto(@Nullable Request request) {
        if (request == null) {
            return null;
        }

        return new RequestDto(
                request.getId(),
                request.getRequestStatus(),
                request.getText(),
                request.getPhoneNumber(),
                request.getName(),
                request.getCreatedAt(),
                userMapper.toDto(request.getUser())
        );
    }

    @Nullable
    public RequestDto toDtoFetchUser(@Nullable Request request, User user) {
        if (request == null) {
            return null;
        }

        return new RequestDto(
                request.getId(),
                request.getRequestStatus(),
                request.getText(),
                request.getPhoneNumber(),
                request.getName(),
                request.getCreatedAt(),
                userMapper.toDto(user)
        );
    }


}
