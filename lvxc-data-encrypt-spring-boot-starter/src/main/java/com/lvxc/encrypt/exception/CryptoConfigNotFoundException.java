package com.lvxc.encrypt.exception;

public class CryptoConfigNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CryptoConfigNotFoundException(String message) {
        super(message);
    }

    public CryptoConfigNotFoundException(Throwable cause) {
        super(cause);
    }

    public CryptoConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
