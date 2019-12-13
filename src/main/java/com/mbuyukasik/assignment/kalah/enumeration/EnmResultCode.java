package com.mbuyukasik.assignment.kalah.enumeration;

/**
 * Custom Code and message that is returned from REST services
 * 
 * @author TTMBUYUKASIK
 *
 */
public enum EnmResultCode {

	SUCCESS("Code-000", "SUCCESS"),
	EXCEPTION("Code-001", "Operation failed with an unexpected error. Please contact service admin for details."),
	GAME_NOT_FOUNT("Code-002", "Game is not found"),
	GAME_OVER("Code-003", "Game is over"),
	INVALID_PIT("Code-004", "Invalid pit id"),
	PIT_EMPTY("Code-005", "Pit is empty");
	
	private String code;
	private String message;
	
	EnmResultCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
