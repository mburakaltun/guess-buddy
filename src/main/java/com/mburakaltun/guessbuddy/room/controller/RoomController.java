package com.mburakaltun.guessbuddy.room.controller;

import com.mburakaltun.guessbuddy.common.constants.AppHeaders;
import com.mburakaltun.guessbuddy.common.controller.BaseController;
import com.mburakaltun.guessbuddy.common.exception.AppException;
import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.room.model.request.RequestCloseRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestCreateRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestGetUserRooms;
import com.mburakaltun.guessbuddy.room.model.request.RequestJoinRoom;
import com.mburakaltun.guessbuddy.room.model.request.RequestLeaveRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCloseRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseCreateRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseGetUserRooms;
import com.mburakaltun.guessbuddy.room.model.response.ResponseJoinRoom;
import com.mburakaltun.guessbuddy.room.model.response.ResponseLeaveRoom;
import com.mburakaltun.guessbuddy.room.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rooms")
public class RoomController extends BaseController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResponseCreateRoom>> createRoom(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                      @RequestBody @Valid RequestCreateRoom requestCreateRoom) throws AppException {
        ResponseCreateRoom response = roomService.createRoom(requestCreateRoom, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<ResponseJoinRoom>> joinRoom(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                  @RequestBody @Valid RequestJoinRoom requestJoinRoom) throws AppException {
        ResponseJoinRoom response = roomService.joinRoom(requestJoinRoom, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }

    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<ResponseLeaveRoom>> leaveRoom(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                    @RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                    @RequestBody @Valid RequestLeaveRoom requestLeaveRoom) throws AppException {
        ResponseLeaveRoom response = roomService.leaveRoom(requestLeaveRoom, userId, roomId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }

    @PostMapping("/close")
    public ResponseEntity<ApiResponse<ResponseCloseRoom>> closeRoom(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                    @RequestHeader(AppHeaders.X_ROOM_ID) Long roomId,
                                                                    @RequestBody @Valid RequestCloseRoom requestCloseRoom) throws AppException {
        ResponseCloseRoom response = roomService.closeRoom(requestCloseRoom, userId, roomId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }

    @GetMapping("/user-rooms")
    public ResponseEntity<ApiResponse<ResponseGetUserRooms>> getUserRooms(@RequestHeader(AppHeaders.X_USER_ID) Long userId,
                                                                          @ModelAttribute @Valid RequestGetUserRooms requestGetUserRooms) throws AppException {
        ResponseGetUserRooms response = roomService.getUserRooms(requestGetUserRooms, userId);
        return new ResponseEntity<>(respond(response), HttpStatus.OK);
    }
}
