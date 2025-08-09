package com.mburakaltun.guessbuddy.user.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.user.model.request.RequestBlockUser;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangePassword;
import com.mburakaltun.guessbuddy.user.model.request.RequestChangeUsername;
import com.mburakaltun.guessbuddy.user.model.request.RequestGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.request.RequestUnblockUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseBlockUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangePassword;
import com.mburakaltun.guessbuddy.user.model.response.ResponseChangeUsername;
import com.mburakaltun.guessbuddy.user.model.response.ResponseDeleteUser;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetBlockedUsers;
import com.mburakaltun.guessbuddy.user.model.response.ResponseGetUserProfile;
import com.mburakaltun.guessbuddy.user.model.response.ResponseUnblockUser;
import com.mburakaltun.guessbuddy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ResponseGetUserProfile>> getUserProfile(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                              @ModelAttribute RequestGetUserProfile requestGetUserProfile) throws AppException {
        ResponseGetUserProfile response = userService.getUserProfile(requestGetUserProfile, userId);
        return ResponseEntity.ok(respond(response));
    }

    @PutMapping("/username")
    public ResponseEntity<ApiResponse<ResponseChangeUsername>> changeUsername(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                              @RequestBody @Valid RequestChangeUsername requestChangeUsername) throws AppException {
        ResponseChangeUsername response = userService.changeUsername(requestChangeUsername, userId);
        return ResponseEntity.ok(respond(response));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<ResponseChangePassword>> changePassword(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                              @RequestBody @Valid RequestChangePassword requestChangePassword) throws AppException {
        ResponseChangePassword response = userService.changePassword(requestChangePassword, userId);
        return ResponseEntity.ok(respond(response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<ResponseDeleteUser>> deleteUser(@RequestHeader(AppHeaders.X_USER_ID) Long userId) throws AppException {
        ResponseDeleteUser response = userService.deleteUser(userId);
        return ResponseEntity.ok(respond(response));
    }

    @PostMapping("/block")
    public ResponseEntity<ApiResponse<ResponseBlockUser>> blockUser(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                    @RequestBody @Valid RequestBlockUser requestBlockUser) throws AppException {
        ResponseBlockUser response = userService.blockUser(requestBlockUser, userId);
        return ResponseEntity.ok(respond(response));
    }

    @PostMapping("/unblock")
    public ResponseEntity<ApiResponse<ResponseUnblockUser>> unblockUser(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                        @RequestBody @Valid RequestUnblockUser requestUnblockUser) throws AppException {
        ResponseUnblockUser response = userService.unblockUser(requestUnblockUser, userId);
        return ResponseEntity.ok(respond(response));
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<ApiResponse<ResponseGetBlockedUsers>> getBlockedUsers(@RequestHeader(AppHeaders.X_USER_ID) Long userId) {
        ResponseGetBlockedUsers response = userService.getBlockedUsers(userId);
        return ResponseEntity.ok(respond(response));
    }
}
