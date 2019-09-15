package ar.edu.itba.exceptions;

public class InvalidQueryParametersException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidQueryParametersException(String message) {
		super(message);
	}
}