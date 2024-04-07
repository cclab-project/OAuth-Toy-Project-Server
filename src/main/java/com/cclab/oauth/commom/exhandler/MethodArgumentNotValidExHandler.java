package com.cclab.oauth.commom.exhandler;

import com.cclab.oauth.commom.costomresponse.reponse.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
@Order(value = 1)
public class MethodArgumentNotValidExHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.info("", ex);

        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(status)
            .body(
                    Response.ERROR(status.value(), defaultMessage)
            );

    }

}
