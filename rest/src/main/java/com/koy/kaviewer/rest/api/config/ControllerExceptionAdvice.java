package com.koy.kaviewer.rest.api.config;

import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {
    @ResponseBody
    @ExceptionHandler
    ResponseEntity<String> exceptionHandler(KaViewerBizException kaViewerBizException) {
        final String exceptionMessage = kaViewerBizException.getMessage();
        final String message = kaViewerBizException.getCause().getMessage();
        return new ResponseEntity<String>(String.join("\n", exceptionMessage, message), HttpStatus.BAD_REQUEST);
    }

}
