package ar.edu.itba.exceptions;

public class InvalidQueryMomentException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidQueryMomentException(String message) {
		super(message);
	}
}
