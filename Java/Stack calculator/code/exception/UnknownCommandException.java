package ru.my.exception;

public class UnknownCommandException extends CalcException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
