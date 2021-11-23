package de.tech26.robotfactory.exception;

/**
 * @author Jagadheeswar Reddy
 *
 */
public class UnProcessableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnProcessableException() {
	}

	public UnProcessableException(String message) {
		super(message);
	}

	public UnProcessableException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnProcessableException(Throwable cause) {
		super(cause);
	}
}