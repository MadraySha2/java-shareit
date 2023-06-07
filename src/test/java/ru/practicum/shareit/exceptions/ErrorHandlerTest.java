package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFoundException() {
        NotFoundException exception = new NotFoundException("Item not found");
        ErrorResponse response = errorHandler.handleNotFoundException(exception);

        assertEquals("Not found error", response.getError());
        assertEquals("Item not found", response.getDescription());
    }

    @Test
    void notAvailableException() {
        NotAvailableException exception = new NotAvailableException("Item not available");
        ErrorResponse response = errorHandler.notAvailableException(exception);

        assertEquals("Available error", response.getError());
        assertEquals("Item not available", response.getDescription());
    }

    @Test
    void duplicateException() {
        CloneNotSupportedException exception = new CloneNotSupportedException("Item already exists");
        ErrorResponse response = errorHandler.duplicateException(exception);

        assertEquals("Duplicate error", response.getError());
        assertEquals("Item already exists", response.getDescription());
    }

    @Test
    void handleThrowable() {
        Throwable exception = new Throwable("Something went wrong");
        ErrorResponse response = errorHandler.handleThrowable(exception);

        assertEquals("Internal Server error", response.getError());
        assertEquals("Something went wrong", response.getDescription());
    }

    @Test
    void handleWrongState() {
        NotSupportedStateException exception = new NotSupportedStateException("Invalid state");
        ErrorResponse response = errorHandler.handleWrongState(exception);

        assertEquals("Invalid state", response.getError());
        assertEquals("Invalid state", response.getDescription());
    }
}
