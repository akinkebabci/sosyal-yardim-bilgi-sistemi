package com.sais.exception;


public class MernisServiceException extends RuntimeException {

    public MernisServiceException(String message) {
        super(message);
    }

    public MernisServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}


