package com.sqy.urms.core.mapper;

import com.sqy.urms.dadataclient.DadataClient;
import com.sqy.urms.dadataclient.dto.PhoneNumberDto;
import com.sqy.urms.dto.requestentity.CreateRequestDto;
import com.sqy.urms.dto.requestentity.RequestDto;
import com.sqy.urms.dto.requestentity.RequestStatus;
import com.sqy.urms.persistence.model.PhoneNumber;
import com.sqy.urms.persistence.model.Request;
import com.sqy.urms.persistence.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.sqy.urms.dadataclient.util.DadataClientUtils.preparePhoneValue;

@Component
public class RequestMapper {

    private final UserMapper userMapper;
    private final DadataClient dadataClient;
    private final PhoneNumberMapper phoneNumberMapper;

    public RequestMapper(UserMapper userMapper, DadataClient dadataClient, PhoneNumberMapper phoneNumberMapper) {
        this.userMapper = userMapper;
        this.dadataClient = dadataClient;
        this.phoneNumberMapper = phoneNumberMapper;
    }

    @Nullable
    public Request fromCreateDto(@Nullable CreateRequestDto createRequestDto, @Nullable Long userId) {
        if (createRequestDto == null || userId == null) {
            return null;
        }
        User requestAuthor = new User();
        requestAuthor.setId(userId);

        PhoneNumberDto phone = dadataClient.getPhone(preparePhoneValue(createRequestDto.phoneNumber())).stream().findAny().orElse(null);
        PhoneNumber phoneNumber = phoneNumberMapper.fromDto(phone);


        return new Request(
                null,
                RequestStatus.DRAFT,
                createRequestDto.text(),
                phoneNumber,
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
                request.getPhoneNumber().getPhone(),
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
        Objects.requireNonNull(user, "User can't be null");

        return new RequestDto(
                request.getId(),
                request.getRequestStatus(),
                request.getText(),
                request.getPhoneNumber().getPhone(),
                request.getName(),
                request.getCreatedAt(),
                userMapper.toDto(user)
        );
    }


}
