package com.mburakaltun.guessbuddy.authentication.controller;

import com.mburakaltun.guessbuddy.authentication.model.request.RequestCompleteForgotPassword;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestStartForgotPassword;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignInUser;
import com.mburakaltun.guessbuddy.authentication.model.request.RequestSignUpUser;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseCompleteForgotPassword;
import com.mburakaltun.guessbuddy.authentication.model.response.ResponseStartForgotPassword;
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

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<ResponseSignUpUser>> signUp(@Valid @RequestBody RequestSignUpUser requestSignUpUser) throws AppException {
        ResponseSignUpUser response = authenticationService.signUpUser(requestSignUpUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(respond(response));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<ResponseSignInUser>> signIn(@Valid @RequestBody RequestSignInUser requestSignInUser) throws AppException {
        ResponseSignInUser response = authenticationService.signInUser(requestSignInUser);
        return ResponseEntity.ok(respond(response));
    }

    @PostMapping("/start-forgot-password")
    public ResponseEntity<ApiResponse<ResponseStartForgotPassword>> startForgotPassword(@Valid @RequestBody RequestStartForgotPassword requestStartForgotPassword) throws AppException {
        ResponseStartForgotPassword response = authenticationService.startForgotPassword(requestStartForgotPassword);
        return ResponseEntity.ok(respond(response));
    }

    @PostMapping("/complete-forgot-password")
    public ResponseEntity<ApiResponse<ResponseCompleteForgotPassword>> completeForgotPassword(@Valid @RequestBody RequestCompleteForgotPassword requestCompleteForgotPassword) throws AppException {
        ResponseCompleteForgotPassword response = authenticationService.completeForgotPassword(requestCompleteForgotPassword);
        return ResponseEntity.ok(respond(response));
    }
}

