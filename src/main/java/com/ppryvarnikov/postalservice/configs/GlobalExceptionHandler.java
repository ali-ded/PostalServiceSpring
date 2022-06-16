package com.ppryvarnikov.postalservice.configs;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationException(Exception ex) {
        log.warn("Handle validation exception '{}': {}", ex.getClass().getSimpleName(), ex.getMessage());
        Map<String, String> response = new HashMap<>(2);
        response.put("Exception", ex.getClass().getSimpleName());
        response.put("Message", ex.getMessage());
        return response;
    }
}

