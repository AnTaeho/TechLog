package com.example.techlog.error.custom;

import org.springframework.http.HttpStatus;

public class CriticalException extends CustomException{

    public CriticalException(HttpStatus errorCode, String message) {
        super(errorCode, message);
    }

}
