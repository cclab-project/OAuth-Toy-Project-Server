package com.cclab.oauth.commom.exhandler;

import com.cclab.oauth.commom.costomresponse.reponse.Response;
import com.cclab.oauth.commom.error.ErrorCodeIfs;
import com.cclab.oauth.commom.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(value = Integer.MIN_VALUE)
public class ApiExHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Response<Object>> apiExceptionHandler(
            ApiException apiException
    ){

        log.error("ApiExceptionHandler", apiException);

        ErrorCodeIfs errorCodeIfs = apiException.getErrorCodeIfs();
        return ResponseEntity
            .status(errorCodeIfs.getHttpStatusCode())
            .body(
                    Response.ERROR(errorCodeIfs.getErrorCode(), apiException.getErrorDescription())
            );

    }
}
