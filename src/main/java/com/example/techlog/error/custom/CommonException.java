package com.example.techlog.error.custom;

import org.springframework.http.HttpStatus;

public class CommonException extends CustomException{

    public CommonException(HttpStatus errorCode, String message) {
        super(errorCode, message);
    }

}
