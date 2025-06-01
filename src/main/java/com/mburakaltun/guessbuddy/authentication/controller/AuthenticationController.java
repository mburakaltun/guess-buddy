package com.mburakaltun.guessbuddy.authentication.controller;

import com.mburakaltun.guessbuddy.authentication.model.request.SignInUserRequest;
import com.mburakaltun.guessbuddy.authentication.model.request.SignUpUserRequest;
import com.mburakaltun.guessbuddy.authentication.model.response.SignInUserResponse;
import com.mburakaltun.guessbuddy.authentication.model.response.SignUpUserResponse;
import com.mburakaltun.guessbuddy.authentication.service.AuthenticationService;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authentication")
public class AuthenticationController extends BaseController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public ResponseEntity<ApiResponse<SignUpUserResponse>> signUp(@Valid @RequestBody SignUpUserRequest signUpUserRequest) throws AppException {
        SignUpUserResponse response = authenticationService.signUpUser(signUpUserRequest);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<SignInUserResponse>> signIn(@Valid @RequestBody SignInUserRequest signInUserRequest) throws AppException {
        SignInUserResponse response = authenticationService.signInUser(signInUserRequest);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }
}

