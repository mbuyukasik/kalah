package com.mbuyukasik.assignment.kalah.model.operationresult;

import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;

/**
 * Used as a return type of methods that return single value. 
 * If operation succeed, resultValue object is filled. 
 * In error case only fields of super class are set to return error 
 * 
 * @author TTMBUYUKASIK
 *
 * @param <T>
 */
public class ValueOperationResult<T> extends OperationResult {

	private T resultValue;

	public T getResultValue() {
		return resultValue;
	}

	public void setResultValue(T resultValue) {
		this.resultValue = resultValue;
	}
	
	public ValueOperationResult() {
		this(null, null);
	}
	
	public ValueOperationResult(EnmResultCode resultCode, T resultValue) {
		super(resultCode, null);
		this.resultValue = resultValue;
	}
	
}
