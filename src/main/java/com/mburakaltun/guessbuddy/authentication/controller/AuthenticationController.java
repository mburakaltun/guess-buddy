package com.mburakaltun.guessbuddy.authentication.controller;

import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignUpUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseSignUpUser;
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
    public ResponseEntity<ApiResponse<ResponseSignUpUser>> signUp(@Valid @RequestBody RequestSignUpUser requestSignUpUser) throws AppException {
        ResponseSignUpUser response = authenticationService.signUpUser(requestSignUpUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(respond(response));
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResponse<ResponseSignInUser>> signIn(@Valid @RequestBody RequestSignInUser requestSignInUser) throws AppException {
        ResponseSignInUser response = authenticationService.signInUser(requestSignInUser);
        return ResponseEntity.ok(respond(response));
    }
}

