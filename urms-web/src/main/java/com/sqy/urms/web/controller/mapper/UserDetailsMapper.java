package com.sqy.urms.web.controller.mapper;

import com.sqy.urms.dto.util.AppUserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class UserDetailsMapper {

    @Nullable
    public AppUserDto toDto(@Nullable UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        return new AppUserDto(userDetails.getUsername(), userDetails.getAuthorities().stream().map(Object::toString).toList());
    }
}
