package org.example.exception;

public class ParserException extends RuntimeException{
    private final int position;

    public ParserException(String message, int position) {
        super(message);
        this.position = position;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (позиция: " + position + ")";
    }
}
