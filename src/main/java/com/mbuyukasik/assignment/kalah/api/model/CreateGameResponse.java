package com.mbuyukasik.assignment.kalah.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response of create  game request
 * 
 * @author TTMBUYUKASIK
 *
 */
@JsonPropertyOrder({"id", "url"})
public class CreateGameResponse {
	
	@JsonProperty
	private String id;
	@JsonProperty
	private String url;
	
	public CreateGameResponse() {
		this(null, null);
	}
	
	public CreateGameResponse(String id, String url) {
		this.id = id;
		this.url = url;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
