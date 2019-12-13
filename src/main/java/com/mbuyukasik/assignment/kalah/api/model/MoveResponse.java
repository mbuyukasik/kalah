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
	private Map<Integer, Integer> status;
	
	public MoveResponse() {
		this(null, null, null);
	}
	
	public MoveResponse(String id, String url, Map<Integer, Integer> status) {
		super(id, url);
		this.status = status;
	}

	public Map<Integer, Integer> getStatus() {
		return status;
	}

	public void setStatus(Map<Integer, Integer> status) {
		this.status = status;
	}
	
}
