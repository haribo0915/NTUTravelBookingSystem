package org.oop18.exceptions;

/**
 * Databse connect exception
 */
public class DBConnectException extends RuntimeException {
	public DBConnectException(String errMessage) {
		super(errMessage);
	}
}
