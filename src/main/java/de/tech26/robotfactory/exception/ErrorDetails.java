package de.tech26.robotfactory.exception;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * ErrorDetails for customize exceptions 
 * @author Jagadheeswar Reddy
 *
 */
public class ErrorDetails {
	private Timestamp timestamp;
	private String message;
	private HttpStatus status;
	private List<String> errors;

	private static ErrorDetails errorDetails = null;

	private ErrorDetails() {
		super();
	}

	private void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public static ErrorDetails getInstance(final HttpStatus status, final String message, final String error) {

		if (errorDetails == null)
			errorDetails = new ErrorDetails();

		return getInstance(status, message, Arrays.asList(error));
	}

	public static ErrorDetails getInstance(final HttpStatus status, final String message, final List<String> errors) {

		if (errorDetails == null)
			errorDetails = new ErrorDetails();

		errorDetails.setTimestamp(Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
		errorDetails.setMessage(message);
		errorDetails.setStatus(status);
		errorDetails.setErrors(errors);
		return errorDetails;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public List<String> getErrors() {
		return errors;
	}

	@Override
	public String toString() {
		return "ErrorDetails [timestamp=" + timestamp + ", message=" + message + ", status=" + status + ", errors="
				+ errors + "]";
	}

}