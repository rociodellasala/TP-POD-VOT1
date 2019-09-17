package ar.edu.itba.exceptions;

public class InvalidVoteOperationException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidVoteOperationException(String message) {
		super(message);
	}

}
