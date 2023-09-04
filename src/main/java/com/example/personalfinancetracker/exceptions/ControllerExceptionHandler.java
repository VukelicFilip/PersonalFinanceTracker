package com.example.personalfinancetracker.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a
	 * 'required' request parameter is missing.
	 *
	 * @param ex
	 *            MissingServletRequestParameterException
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ResourceError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is
	 * invalid as well.
	 *
	 * @param ex
	 *            HttpMediaTypeNotSupportedException
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ResourceError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(
				new com.insyde.organization.error.ResourceError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object
	 * fails @Valid validation.
	 *
	 * @param ex
	 *            the MethodArgumentNotValidException that is thrown when @Valid
	 *            validation fails
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ResourceError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST);
		error.setMessage("Validation error");
		error.addValidationErrors(ex.getBindingResult().getFieldErrors());
		error.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(error);
	}

	
	
	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown
	 * when @Validated fails.
	 *
	 * @param ex
	 *            the ConstraintViolationException
	 * @return the ResourceError object
	 */
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST);
		error.setMessage("Validation error");
		error.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(error);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex
	 *            HttpMessageNotReadableException
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ResourceError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex
	 *            HttpMessageNotWritableException
	 * @param headers
	 *            HttpHeaders
	 * @param status
	 *            HttpStatus
	 * @param request
	 *            WebRequest
	 * @return the ResourceError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = "Error writing JSON output";
		System.out.println("ERROR handleHttpMessageNotWritable :::: " + ex);
		return buildResponseEntity(new com.insyde.organization.error.ResourceError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST);
		error.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		error.setDebugMessage(ex.getMessage());
		return buildResponseEntity(error);
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different
	 * DB causes.
	 *
	 * @param ex
	 *            the DataIntegrityViolationException
	 * @return the ResourceError object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new com.insyde.organization.error.ResourceError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		System.out.println("ERROR DataIntegrityViolationException.class :::: " + ex);
		return buildResponseEntity(new com.insyde.organization.error.ResourceError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle MethodArgumentTypeMismatchException
	 *
	 * @param ex
	 *            the MethodArgumentTypeMismatchException
	 * @return the ResourceError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST);
		error.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		error.setDebugMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle ValidationException
	 *
	 * @param ex
	 *            the ValidationException
	 * @return the ResourceError object
	 */
	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<Object> handleValidationException(ValidationException ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle ForbiddenException
	 *
	 * @param ex
	 *            the Exception
	 * @return the ResourceError object
	 */
	@ExceptionHandler(com.insyde.organization.error.ForbiddenException.class)
	protected ResponseEntity<Object> handleForbiddenException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.FORBIDDEN);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle HttpHostConnectException
	 *
	 * @param ex
	 *            the Exception
	 * @return the ResourceError object
	 */
	@ExceptionHandler(com.insyde.organization.error.HttpHostConnectException.class)
	protected ResponseEntity<Object> handleHttpHostConnectException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.SERVICE_UNAVAILABLE);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle NotFoundException
	 *
	 * @param ex
	 *            the Exception
	 * @return the ResourceError object
	 */
	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<Object> handleNotFoundException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.NOT_FOUND);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle UnauthorizedException
	 *
	 * @param ex
	 *            the Exception
	 * @return the ResourceError object
	 */
	@ExceptionHandler(com.insyde.organization.error.UnauthorizedException.class)
	protected ResponseEntity<Object> handleUnauthorizedException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.UNAUTHORIZED);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex
	 *            the Exception
	 * @return the ResourceError object
	 */
	//Reminder - UNAUTHORIZED!!!
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGenericException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setMessage("Something is wrong... " + ex.getMessage());
		System.out.println("ERROR Exception.class :::: " + ex);
		ex.printStackTrace();
		return buildResponseEntity(error);
	}
	
	/**
	 * Handle AccessDeniedException, handle generic AccessDeniedException.class
	 *
	 * @param ex
	 *            the AccessDeniedException
	 * @return the ResourceError object
	 */
//	@ExceptionHandler(AccessDeniedException.class)
//	protected ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
//		ResourceError error = new ResourceError(HttpStatus.UNAUTHORIZED);
//		error.setMessage("Token missing... " + ex.getMessage());
//		System.out.println("ERROR AccessDeniedException.class :::: " + ex);
//		ex.printStackTrace();
//		return buildResponseEntity(error);
//	}

	private ResponseEntity<Object> buildResponseEntity(com.insyde.organization.error.ResourceError error) {
		return new ResponseEntity<>(error, error.getStatus());
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(Exception ex) {
		com.insyde.organization.error.ResourceError error = new com.insyde.organization.error.ResourceError(HttpStatus.FORBIDDEN);
		error.setMessage(ex.getMessage());
		return buildResponseEntity(error);
	}
	
}
