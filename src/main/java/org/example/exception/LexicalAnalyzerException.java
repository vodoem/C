package org.example.exception;

public class LexicalAnalyzerException extends RuntimeException {
    private final int position;

    public LexicalAnalyzerException(String message, int position) {
        super(message);
        this.position = position;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (позиция: " + position + ")";
    }
}
