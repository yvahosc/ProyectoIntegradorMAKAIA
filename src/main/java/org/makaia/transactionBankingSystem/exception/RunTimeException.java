package org.makaia.transactionBankingSystem.exception;

import java.time.LocalDateTime;

public class RunTimeException {
    private LocalDateTime timestamp;
    private int status;
    private String error;

    public RunTimeException(int status, String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
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

    public String getError() {
        return error;
    }

    public void setErrors(String error) {
        this.error = error;
    }
}

