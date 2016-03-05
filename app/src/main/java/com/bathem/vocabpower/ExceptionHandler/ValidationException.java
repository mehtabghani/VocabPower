package com.bathem.vocabpower.ExceptionHandler;

/**
 * Created by mehtab on 3/5/16.
 */
public class ValidationException extends Exception {

    private String errorMessage;

    public ValidationException(String _description) {
        setErrorMessage(_description);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
