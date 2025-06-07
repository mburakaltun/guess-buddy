package com.mburakaltun.guessbuddy.authentication.service;

import com.mburakaltun.guessbuddy.authentication.model.entity.PasswordResetTokenEntity;
import com.mburakaltun.guessbuddy.authentication.model.entity.UserEntity;
import com.mburakaltun.guessbuddy.authentication.model.enums.AuthenticationErrorCode;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestStartForgotPassword;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignUpUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseStartForgotPassword;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignUpUser;
import com.mburakaltun.guessbuddy.authentication.repository.PasswordResetTokenJpaRepository;
import com.mburakaltun.guessbuddy.user.repository.UserJpaRepository;
import com.mburakaltun.guessbuddy.common.model.enums.AuthorizationRole;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.util.JwtUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender javaMailSender;
    private final PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;

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

    public ResponseStartForgotPassword startForgotPassword(RequestStartForgotPassword requestStartForgotPassword) throws AppException {
        String email = requestStartForgotPassword.getEmail();
        UserEntity userEntity = userJpaRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User not found with email: {}", email);
            return new AppException(AuthenticationErrorCode.USER_NOT_FOUND);
        });

        String token = UUID.randomUUID().toString();

        createPasswordResetToken(userEntity, token);
        sendPasswordResetEmail(userEntity, token);

        return ResponseStartForgotPassword.builder()
                .emailSent(true)
                .build();
    }

    private void sendPasswordResetEmail(UserEntity userEntity, String token) {
        String email = userEntity.getEmail();
        String subject = "Password Reset Request";
        String message = getMessage(userEntity, token);

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);
        javaMailSender.send(emailMessage);

        log.info("Password reset email sent to: {}", email);
    }

    private static String getMessage(UserEntity userEntity, String token) {
        String resetLink = String.format("http://localhost:8080/reset-password?token=%s", token);

        return String.format("Hello %s,\n\n" +
                        "You have requested to reset your password. Please click the link below to reset your password:\n" +
                        "%s\n\n" +
                        "If you did not request this, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "GuessBuddy Team",
                userEntity.getUsername(), resetLink);
    }

    private void createPasswordResetToken(UserEntity userEntity, String token) {
        Optional<PasswordResetTokenEntity> passwordResetTokenEntityOptional = passwordResetTokenJpaRepository.findByUserEntity_Id(userEntity.getId());
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenEntityOptional.orElseGet(PasswordResetTokenEntity::new);
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserEntity(userEntity);
        passwordResetTokenJpaRepository.save(passwordResetTokenEntity);
    }
}