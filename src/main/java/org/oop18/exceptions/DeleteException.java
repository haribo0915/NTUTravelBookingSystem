package org.oop18.exceptions;

/**
 * @author - Haribo
 */
public class DeleteException extends RuntimeException {
    public DeleteException(String errMessage) {
        super(errMessage);
    }
}
