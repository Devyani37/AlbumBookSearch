package com.devyani.kramphub.BookAlbumSearch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SearchControllerExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericHandler(Exception ex, WebRequest request) {
        ErrorResponse errors = new ErrorResponse();
        errors.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
        errors.setMessage(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentHandler(Exception ex, WebRequest request) {
        ErrorResponse errors = new ErrorResponse();
        errors.setError(HttpStatus.BAD_REQUEST.name());
        errors.setMessage(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}
