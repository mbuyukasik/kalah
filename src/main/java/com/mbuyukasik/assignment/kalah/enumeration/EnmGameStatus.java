package com.mbuyukasik.assignment.kalah.enumeration;

/**
 * All possible values of Game Status
 * 
 * @author TTMBUYUKASIK
 *
 */
public enum EnmGameStatus {

	CREATED("CREATED"), 
	PLAYING("PLAYING"), 
	OVER("OVER");

	private String id;

	EnmGameStatus(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

}
