package de.tech26.robotfactory.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.tech26.robotfactory.exception.ErrorDetails;
import de.tech26.robotfactory.exception.UnProcessableException;
import de.tech26.robotfactory.exception.ValidationException;

/**
 * Global exception handler for our robot factory application
 * 
 * @author Jagadheeswar Reddy
 *
 */
@ControllerAdvice
public class RobotFactoryExceptionHandler extends ResponseEntityExceptionHandler {

	ErrorDetails errorDetails = null;

	/* ValidationException */
	@ExceptionHandler({ ValidationException.class })
	public ResponseEntity<Object> validationException(final Exception ex, final WebRequest request) {
		errorDetails = ErrorDetails.getInstance(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), ex.getMessage());

		logger.info(ex.getClass().getName());
		logger.error(ex.getClass().getName() + ":" + errorDetails.toString());

		return new ResponseEntity<Object>(errorDetails, new HttpHeaders(), errorDetails.getStatus());
	}

	/* UnProcessableException */
	@ExceptionHandler({ UnProcessableException.class })
	public ResponseEntity<Object> unProcessableException(final Exception ex, final WebRequest request) {
		errorDetails = ErrorDetails.getInstance(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(),
				ex.getMessage());

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return new ResponseEntity<Object>(errorDetails, new HttpHeaders(), errorDetails.getStatus());
	}

	/* 400 - handleMethodArgumentNotValid */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		final List<String> errors = new ArrayList<String>();

		ex.getBindingResult().getFieldErrors().stream()
				.forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
		ex.getBindingResult().getGlobalErrors().stream()
				.forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

		errorDetails = ErrorDetails.getInstance(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
	}

	/* 404 - handleNoHandlerFoundException */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		errorDetails = ErrorDetails.getInstance(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return new ResponseEntity<Object>(error, new HttpHeaders(), errorDetails.getStatus());
	}

	/* 405 - handleHttpRequestMethodNotSupported */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		errorDetails = ErrorDetails.getInstance(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(),
				builder.toString());

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return new ResponseEntity<Object>(errorDetails, new HttpHeaders(), errorDetails.getStatus());
	}

	/* 415 - handleHttpMediaTypeNotSupported */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		errorDetails = ErrorDetails.getInstance(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(),
				builder.substring(0, builder.length() - 2));

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return new ResponseEntity<Object>(errorDetails, new HttpHeaders(), errorDetails.getStatus());
	}

	/* 500 - handleAllTypesOfExceptions */
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAllTypesOfExceptions(final Exception ex, final WebRequest request) {

		final ErrorDetails errorDetails = ErrorDetails.getInstance(HttpStatus.INTERNAL_SERVER_ERROR,
				ex.getLocalizedMessage(), "error occurred");

		logger.info(ex.getClass().getName());
		logger.error(errorDetails.toString());

		return new ResponseEntity<Object>(errorDetails, new HttpHeaders(), errorDetails.getStatus());
	}

}