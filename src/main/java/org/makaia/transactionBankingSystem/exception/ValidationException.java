package org.makaia.transactionBankingSystem.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationException {
    private LocalDateTime timestamp;
    private int status;
    private List<String> errors;

    public ValidationException(int status, List<String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

