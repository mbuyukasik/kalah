package com.mbuyukasik.assignment.kalah.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** 
 * MoveEntity has the mapping with MOVE database table
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Entity(name = "MOVE")
public class MoveEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
	@Column(name="game_id")
    private Long gameId;
	
	@Column(name="player_id")
    private Long playerId;
	
	@Column(name="seq")
    private Integer seq;
	
	@Column(name="pit_id")
    private Integer pitId;

	@Column(name="pit_status")
    private String pitStatus;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getPitId() {
		return pitId;
	}

	public void setPitId(Integer pitId) {
		this.pitId = pitId;
	}

	public String getPitStatus() {
		return pitStatus;
	}

	public void setPitStatus(String pitStatus) {
		this.pitStatus = pitStatus;
	}
	
}
