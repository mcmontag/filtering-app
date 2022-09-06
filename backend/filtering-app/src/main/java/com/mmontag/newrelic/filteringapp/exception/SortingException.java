package com.mmontag.newrelic.filteringapp.exception;

public class SortingException extends Exception {

    public SortingException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // No need for a stack trace, this is more of a signal than a "true" Exception. 
        return this;
    }
    
}
