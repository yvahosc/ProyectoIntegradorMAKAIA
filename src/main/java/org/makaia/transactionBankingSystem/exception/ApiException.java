package org.makaia.transactionBankingSystem.exception;

import java.util.List;

public class ApiException extends Throwable{

    private int statusCode;
    private List<String> errors;

    /*
    public ApiException(String mensaje){
        super(mensaje);
    }

     */

    public ApiException(int statusCode, List<String> errors){
        this.errors = errors;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<String> getErrors() {
        return errors;
    }
}
