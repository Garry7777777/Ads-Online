package com.skypro.adsonline.exception.handler;

import com.skypro.adsonline.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceEx {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> NotFound() { return ResponseEntity.notFound().build();}

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> Forbidden() { return ResponseEntity.status(HttpStatus.FORBIDDEN).build();}

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> Unauthorized() { return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}

    @ExceptionHandler(InvalidParametersException.class)
    public ResponseEntity<?> invalidParam() { return ResponseEntity.status(400).build();}

}
