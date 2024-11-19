package com.bichotas.moduloprestamos.exception;

/**
 * Represents a generic exception for the Prestamos (Loans) module.
 * This class serves as the base exception for other specific exceptions
 * related to the loan management system.
 */
public class PrestamosException extends RuntimeException {
    /**
     * Constructs a new PrestamosException with the specified detail message.
     *
     * @param message the detail message for the exception.
     */
    public PrestamosException(String message) {
        super(message);
    }

    /**
     * Represents an exception related to time errors in the Prestamos module.
     * This might be used for cases where a loan request violates time constraints.
     */
    public static class PrestamosExceptionTimeError extends PrestamosException {
        /**
         * Constructs a new PrestamosExceptionTimeError with the specified detail message.
         *
         * @param message the detail message for the exception.
         */
        public PrestamosExceptionTimeError(String message) {
            super(message);
        }
    }

    /**
     * Represents an exception related to state errors in the Prestamos module.
     * This might be used for cases where the state of an operation is invalid or unexpected.
     */
    public static class PrestamosExceptionStateError extends PrestamosException {
        /**
         * Constructs a new PrestamosExceptionStateError with the specified detail message.
         *
         * @param message the detail message for the exception.
         */
        public PrestamosExceptionStateError(String message) {
            super(message);
        }
    }

    /**
     * Represents an exception indicating that a student already has an active loan.
     * This might be used to enforce rules preventing multiple active loans for a single student.
     */
    public static class PrestamosExceptionEstudianteHasPrestamo extends PrestamosException {
        /**
         * Constructs a new PrestamosExceptionEstudianteHasPrestamo with the specified detail message.
         *
         * @param message the detail message for the exception.
         */
        public PrestamosExceptionEstudianteHasPrestamo(String message) {
            super(message);
        }
    }

    /**
     * Represents an exception indicating that a book is already available.
     * This might be used for cases where an operation assumes the book is unavailable but it is not.
     */
    public static class PrestamosExceptionBookIsAvailable extends PrestamosException {
        /**
         * Constructs a new PrestamosExceptionBookIsAvailable with the specified detail message.
         *
         * @param message the detail message for the exception.
         */
        public PrestamosExceptionBookIsAvailable(String message) {
            super(message);
        }
    }
}
