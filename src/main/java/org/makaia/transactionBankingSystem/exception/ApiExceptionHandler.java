package org.makaia.transactionBankingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice()
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ValidationException> handler(ApiException e){
        ValidationException exception = new ValidationException(e.getStatusCode(), e.getErrors());
        return new ResponseEntity<ValidationException>(exception, HttpStatus.valueOf(e.getStatusCode()));
    }


    /*
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<RunTimeException> handler(RuntimeException e){
        RunTimeException exception = new RunTimeException(500, e.getMessage());
        System.out.println("gdg");
        return new ResponseEntity<RunTimeException>(exception, HttpStatus.valueOf(exception.getStatus()));
    }

     */
}
