package com.mburakaltun.guessbuddy.authentication.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignUpUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignUpUser;
import com.mburakaltun.guessbuddy.authentication.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.common.model.enums.AuthorizationRole;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final AuthenticationManager authenticationManager;

    public ResponseSignUpUser signUpUser(RequestSignUpUser requestSignUpUser) throws AppException {
        String email = requestSignUpUser.getEmail();
        String password = requestSignUpUser.getPassword();
        String username = requestSignUpUser.getUsername();

        validateEmail(email);
        validateUsername(username);
        validatePasswordMatch(requestSignUpUser);

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setUsername(username);
        userEntity.setEncodedPassword(encodedPassword);
        userEntity.setRole(AuthorizationRole.ROLE_STANDARD_USER);
        userJpaRepository.save(userEntity);

        log.info("User signed up successfully with email: {}", email);
        return ResponseSignUpUser.builder()
                .id(userEntity.getId())
                .build();
    }

    public ResponseSignInUser signInUser(RequestSignInUser requestSignInUser) throws AppException {
        String email = requestSignInUser.getEmail();
        String password = requestSignInUser.getPassword();

        UserEntity userEntity = validateUserCredentials(email, password);
        Authentication authentication = authenticateUser(email, password);

        AuthorizationRole role = generateRole(authentication);
        String authenticationToken = JwtUtility.generateToken(email, role);

        log.info("User signed in successfully with email: {}", email);
        return ResponseSignInUser.builder()
                .authenticationToken(authenticationToken)
                .userId(String.valueOf(userEntity.getId()))
                .username(userEntity.getUsername())
                .build();
    }

    private AuthorizationRole generateRole(Authentication authentication) {
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        return AuthorizationRole.valueOf(role);
    }

    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, password
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void validateEmail(String email) throws AppException {
        boolean isEmailExist = userJpaRepository.existsByEmail(email);
        if (isEmailExist) {
            log.error("Email already exists: {}", email);
            throw new AppException(AuthenticationErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }


    private void validateUsername(String username) throws AppException {
        boolean isUsernameExist = userJpaRepository.existsByUsername(username);
        if (isUsernameExist) {
            log.error("Username already exists: {}", username);
            throw new AppException(AuthenticationErrorCode.USERNAME_ALREADY_EXISTS);
        }
    }

    private UserEntity validateUserCredentials(String email, String password) throws AppException {
        UserEntity userEntity = userJpaRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Incorrect email or password: {}", email);
            return new AppException(AuthenticationErrorCode.INVALID_CREDENTIALS);
        });

        String encodedPassword = userEntity.getEncodedPassword();
        boolean isPasswordCorrect = passwordEncoder.matches(password, encodedPassword);
        if (!isPasswordCorrect) {
            log.error("Incorrect email or password: {}", email);
            throw new AppException(AuthenticationErrorCode.INVALID_CREDENTIALS);
        }
        return userEntity;
    }

    private void validatePasswordMatch(RequestSignUpUser requestSignUpUser) throws AppException {
        String password = requestSignUpUser.getPassword();
        String confirmPassword = requestSignUpUser.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new AppException(AuthenticationErrorCode.PASSWORD_MISMATCH);
        }
    }
}