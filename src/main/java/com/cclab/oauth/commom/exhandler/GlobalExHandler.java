package com.cclab.oauth.commom.exhandler;

import com.cclab.oauth.commom.costomresponse.reponse.Response;
import com.cclab.oauth.commom.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
@Slf4j
public class GlobalExHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response<?>> globalExHandler(
            Exception e
    ){
        log.error("", e);

        return ResponseEntity
                .status(500)
                .body(Response.ERROR(ErrorCode.SERVER_ERROR));
    }
}
