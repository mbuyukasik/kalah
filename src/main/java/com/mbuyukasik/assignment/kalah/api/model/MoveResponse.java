package com.mbuyukasik.assignment.kalah.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response of move operation. Returns latest status of game
 * 
 * @author TTMBUYUKASIK
 *
 */
@JsonPropertyOrder({"id", "uri", "status"})
public class MoveResponse extends CreateGameResponse {

	@JsonProperty
	private Map<String, String> status;
	
	public MoveResponse() {
		this(null, null, null);
	}
	
	public MoveResponse(String id, String url, Map<String, String> status) {
		super(id, url);
		this.status = status;
	}

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}
	
}
