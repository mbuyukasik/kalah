package com.mbuyukasik.assignment.kalah.model.operationresult;

import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;

/**
 * Used to return understandable result from methods
 * 
 * @author TTMBUYUKASIK
 *
 */
public class OperationResult {

	private EnmResultCode resultCode;
	private String resultDesc;

	public EnmResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(EnmResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public OperationResult(EnmResultCode resultCode, String resultDesc) {
		this.resultCode = resultCode;
		this.resultDesc = resultDesc;
	}

	public static boolean isResultSucces(OperationResult operationResult) {
		if (operationResult != null && operationResult.getResultCode() != null
				&& operationResult.getResultCode().equals(EnmResultCode.SUCCESS)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getResultDesc(OperationResult operationResult) {
		if (operationResult != null && operationResult.getResultDesc() != null) {
			return operationResult.getResultDesc();
		} else {
			return "";
		}
	}

}
