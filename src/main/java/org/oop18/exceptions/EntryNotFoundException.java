package org.oop18.exceptions;

public class EntryNotFoundException extends RuntimeException {
	public EntryNotFoundException(String message) {
		super(message);
	}
}
