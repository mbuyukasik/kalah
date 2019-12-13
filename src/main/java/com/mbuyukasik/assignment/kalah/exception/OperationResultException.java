package com.mbuyukasik.assignment.kalah.exception;

import org.springframework.http.HttpStatus;

import com.mbuyukasik.assignment.kalah.model.operationresult.OperationResult;

/**
 * Used to throw custom exception. 
 * OperationResult property is used to read result of an operation in detail
 * 
 * @author TTMBUYUKASIK
 *
 */
public class OperationResultException extends RuntimeException {

	private static final long serialVersionUID = 2597886090209281575L;

	private OperationResult operationResult;
	private HttpStatus httpStatus;

	public OperationResultException(OperationResult operationResult) {
		this(operationResult, null);
	}
	
	public OperationResultException(OperationResult operationResult, HttpStatus httpStatus) {
		super(operationResult.getResultDesc());
		this.operationResult = operationResult;
		this.httpStatus = httpStatus;
	}
	
	public OperationResult getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(OperationResult operationResult) {
		this.operationResult = operationResult;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
