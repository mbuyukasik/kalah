package com.mbuyukasik.assignment.kalah.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * This response type is used return custom error code
 * 
 * @author TTMBUYUKASIK
 *
 */
@JsonPropertyOrder({"code", "message"})
public class ErrorResponse {
	
	@JsonProperty
    private String code;
	
	@JsonProperty
    private String message;

	public ErrorResponse() {
		this(null, null);
	}
	
	public ErrorResponse(String code, String message) {
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
