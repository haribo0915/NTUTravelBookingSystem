package org.oop18.exceptions;

/**
 * @author - Haribo
 */
public class QueryException extends RuntimeException {
    public QueryException(String errMessage) {
        super(errMessage);
    }
}
