package com.sqy.urms.core.mapper;

import com.sqy.urms.dto.user.UserDto;
import com.sqy.urms.persistence.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class UserMapper {

    @Nullable
    public UserDto toDto(@Nullable User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getLogin(), user.getName());
    }
}
