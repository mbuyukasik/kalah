package com.mbuyukasik.assignment.kalah.api.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mbuyukasik.assignment.kalah.api.model.ErrorResponse;
import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;
import com.mbuyukasik.assignment.kalah.exception.OperationResultException;

/**
 * KalahExceptionHandler is a customized exception handler to return suitable
 * REST response in case of error
 * 
 * @author TTMBUYUKASIK
 *
 */
@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class KalahExceptionHandler extends ResponseEntityExceptionHandler {

	String customLogFormat = "Error Code: %s, Error Description: %s";

	private Logger LOG = LoggerFactory.getLogger(KalahExceptionHandler.class);

	/**
	 * Exception listener method. If exception is in type of OperationResultException 
	 * return status and error code are taken from exception, otherwise general 500 status is returned
	 * 
	 * @param exception
	 * @param request
	 * @return ResponseEntity<ErrorResponse> object which is customized for
	 *         different exception types
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {

		String errorMessage = null;
		ErrorResponse errorResponse = null;
		HttpStatus httpStatus = null;

		if (exception instanceof OperationResultException) {
			OperationResultException operationResultEx = ((OperationResultException) exception);
			errorMessage = String.format(customLogFormat, operationResultEx.getOperationResult().getResultCode(),
					operationResultEx.getOperationResult().getResultDesc());
			httpStatus = operationResultEx.getHttpStatus();
			errorResponse = new ErrorResponse(operationResultEx.getOperationResult().getResultCode().getCode(),
					operationResultEx.getOperationResult().getResultCode().getMessage());
		} else {
			errorMessage = exception.getMessage();
			errorResponse = new ErrorResponse(EnmResultCode.EXCEPTION.getCode(), exception.getMessage());
		}

		LOG.error(errorMessage, exception);

		if (httpStatus == null) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<ErrorResponse>(errorResponse, httpStatus);
	}
}