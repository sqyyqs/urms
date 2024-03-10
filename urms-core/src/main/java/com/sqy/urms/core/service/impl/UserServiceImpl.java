package com.sqy.urms.core.service.impl;

import com.sqy.urms.core.mapper.TokenMapper;
import com.sqy.urms.core.mapper.UserMapper;
import com.sqy.urms.core.service.UserService;
import com.sqy.urms.core.util.JwtUtils;
import com.sqy.urms.dto.loginout.LoginRequest;
import com.sqy.urms.dto.loginout.LoginTokenResponse;
import com.sqy.urms.dto.token.Token;
import com.sqy.urms.dto.user.UserDto;
import com.sqy.urms.persistence.model.BlackListToken;
import com.sqy.urms.persistence.model.User;
import com.sqy.urms.persistence.repository.BlackListTokenRepository;
import com.sqy.urms.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sqy.urms.persistence.model.UserRole.OPERATOR;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;
    private final BlackListTokenRepository blackListTokenRepository;

    public UserServiceImpl(UserRepository userRepository, TokenMapper tokenMapper, UserMapper userMapper, BlackListTokenRepository blackListTokenRepository) {
        this.userRepository = userRepository;
        this.tokenMapper = tokenMapper;
        this.userMapper = userMapper;
        this.blackListTokenRepository = blackListTokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<LoginTokenResponse> login(LoginRequest loginRequest) {
        logger.info("Invoke login({}).", loginRequest.login());
        User user = userRepository.findByLoginAndPassword(loginRequest.login(), loginRequest.password());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(authority -> authorities.add(authority.name()));

        Instant now = Instant.now();
        Token token = new Token(UUID.randomUUID(), user.getLogin(), authorities, now, now.plus(Duration.ofHours(1)));

        String jwt = tokenMapper.toJWT(token);
        if (!StringUtils.hasText(jwt)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new LoginTokenResponse(jwt, token.expiresAt().toString()));
    }


    @Override
    @Transactional
    public boolean blacklistToken(String authorizationHeader) {
        logger.info("Invoke blacklistToken({}).", authorizationHeader);
        String jwt = JwtUtils.extractToken(authorizationHeader);
        if (jwt == null) {
            return false;
        }

        Token token = tokenMapper.fromJWT(jwt);
        if (token == null || token.expiresAt().isBefore(Instant.now())) {
            return false;
        }

        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setValue(jwt);
        blackListTokenRepository.save(blackListToken);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTokenBlacklisted(String jwtToken) {
        logger.info("Invoke isTokenBlacklisted({}).", jwtToken);
        return blackListTokenRepository.existsByValue(jwtToken);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDto>> getAll() {
        logger.info("Invoke getAll().");
        return ResponseEntity.ok(userRepository.findAll(Sort.by("id").ascending()).stream().map(userMapper::toDto).toList());
    }

    @Override
    @Transactional
    public ResponseEntity<UserDto> makeOpp(long id) {
        logger.info("Invoke makeOpp({}).", id);

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        User result = null;
        if (!user.getAuthorities().contains(OPERATOR)) {
            user.getAuthorities().add(OPERATOR);
            result = userRepository.save(user);
        }
        UserDto dto = userMapper.toDto(result);
        return ResponseEntity.ofNullable(dto);
    }
}
