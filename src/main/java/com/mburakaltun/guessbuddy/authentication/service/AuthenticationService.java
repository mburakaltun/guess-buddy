package com.mburakaltun.guessbuddy.authentication.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.authentication.model.request.SignInUserRequest;
import com.mburakaltun.guessbuddy.authentication.model.request.SignUpUserRequest;
import com.mburakaltun.guessbuddy.authentication.model.response.SignInUserResponse;
import com.mburakaltun.guessbuddy.authentication.model.response.SignUpUserResponse;
import com.mburakaltun.guessbuddy.authentication.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public SignUpUserResponse signUpUser(SignUpUserRequest signUpUserRequest) throws AppException {
        String email = signUpUserRequest.getEmail();
        String password = signUpUserRequest.getPassword();

        validateEmail(email);
        validatePasswordMatch(signUpUserRequest);

        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setEncodedPassword(encodedPassword);
        userEntity.setRole(AuthorizationRole.ROLE_STANDARD_USER);
        userRepository.save(userEntity);

        log.info("User signed up successfully with email: {}", email);
        return SignUpUserResponse.builder()
                .id(userEntity.getId())
                .build();
    }

    public SignInUserResponse signInUser(SignInUserRequest signInUserRequest) throws AppException {
        String email = signInUserRequest.getEmail();
        String password = signInUserRequest.getPassword();

        validateUserCredentials(email, password);
        Authentication authentication = authenticateUser(email, password);

        AuthorizationRole role = generateRole(authentication);
        String authenticationToken = JwtUtility.generateToken(email, role);

        log.info("User signed in successfully with email: {}", email);
        return SignInUserResponse.builder()
                .authenticationToken(authenticationToken)
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
        boolean isEmailExist = userRepository.existsByEmail(email);
        if (isEmailExist) {
            log.error("Email already exists: {}", email);
            throw new AppException(AuthenticationErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void validateUserCredentials(String email, String password) throws AppException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Incorrect email or password: {}", email);
            return new AppException(AuthenticationErrorCode.INVALID_CREDENTIALS);
        });

        String encodedPassword = userEntity.getEncodedPassword();
        boolean isPasswordCorrect = passwordEncoder.matches(password, encodedPassword);
        if (!isPasswordCorrect) {
            log.error("Incorrect email or password: {}", email);
            throw new AppException(AuthenticationErrorCode.INVALID_CREDENTIALS);
        }
    }

    private void validatePasswordMatch(SignUpUserRequest signUpUserRequest) throws AppException {
        String password = signUpUserRequest.getPassword();
        String confirmPassword = signUpUserRequest.getConfirmPassword();
        if (!password.equals(confirmPassword)) {
            throw new AppException(AuthenticationErrorCode.PASSWORD_MISMATCH);
        }
    }
}