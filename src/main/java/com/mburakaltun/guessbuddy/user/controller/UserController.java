package com.mburakaltun.guessbuddy.user.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.user.model.request.RequestGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetUserProfile;
import com.mburakaltun.guessbuddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ResponseGetUserProfile>> getUserProfile(@RequestHeader(AppHeaders.X_USER_ID) String userId,
                                                                              @ModelAttribute RequestGetUserProfile requestGetUserProfile) throws AppException {
        ResponseGetUserProfile response = userService.getUserProfile(requestGetUserProfile, Long.valueOf(userId));
        return ResponseEntity.ok(respond(response));
    }
}
