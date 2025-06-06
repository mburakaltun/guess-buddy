package com.mburakaltun.guessbuddy.common.controller;

import com.mburakaltun.guessbuddy.common.model.response.ApiResponse;
import com.mburakaltun.guessbuddy.common.util.ResponseUtility;

public class BaseController {
    public <T> ApiResponse<T> respond(T data) {
        return ResponseUtility.success(data);
    }
}
