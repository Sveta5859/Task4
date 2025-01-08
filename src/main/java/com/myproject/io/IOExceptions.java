package com.myproject.io;

public class IOExceptions {
    public static class InvalidModelFileException extends Exception {
        public InvalidModelFileException(String msg) {
            super(msg);
        }
    }
}
