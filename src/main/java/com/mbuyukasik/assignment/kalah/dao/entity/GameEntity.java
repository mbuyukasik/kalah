package com.mbuyukasik.assignment.kalah.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** 
 * GameEntity has the mapping with GAME database table
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Entity(name = "GAME")
public class GameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="code")
    private String code;
    
    @Column(name="number_of_stones")
    private int numberOfStones;
    
    @Column(name="player1_id")
    private Long player1Id;
    
    @Column(name="player2_id")
    private Long player2Id;
    
    @Column(name="next_player_id")
    private Long nextPlayerId;
    
    @Column(name="last_move_seq")
    private Integer lastMoveSeq;
    
    @Column(name="pit_status")
    private String pitStatus;
    
    @Column(name="status")
    private String status;
    
    @Column(name="player1_score")
    private Integer player1Scrore;
    
    @Column(name="player2_score")
    private Integer player2Scrore;
    
    @Column(name="winner_player_id")
    private Long winnerPlayerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getNumberOfStones() {
		return numberOfStones;
	}

	public void setNumberOfStones(int numberOfStones) {
		this.numberOfStones = numberOfStones;
	}

	public Long getPlayer1Id() {
		return player1Id;
	}

	public void setPlayer1Id(Long player1Id) {
		this.player1Id = player1Id;
	}

	public Long getPlayer2Id() {
		return player2Id;
	}

	public void setPlayer2Id(Long player2Id) {
		this.player2Id = player2Id;
	}

	public Long getNextPlayerId() {
		return nextPlayerId;
	}

	public void setNextPlayerId(Long nextPlayerId) {
		this.nextPlayerId = nextPlayerId;
	}

	public Integer getLastMoveSeq() {
		return lastMoveSeq;
	}

	public void setLastMoveSeq(Integer lastMoveSeq) {
		this.lastMoveSeq = lastMoveSeq;
	}

	public String getPitStatus() {
		return pitStatus;
	}

	public void setPitStatus(String pitStatus) {
		this.pitStatus = pitStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPlayer1Scrore() {
		return player1Scrore;
	}

	public void setPlayer1Scrore(Integer player1Scrore) {
		this.player1Scrore = player1Scrore;
	}

	public Integer getPlayer2Scrore() {
		return player2Scrore;
	}

	public void setPlayer2Scrore(Integer player2Scrore) {
		this.player2Scrore = player2Scrore;
	}

	public Long getWinnerPlayerId() {
		return winnerPlayerId;
	}

	public void setWinnerPlayerId(Long winnerPlayerId) {
		this.winnerPlayerId = winnerPlayerId;
	}
    
}
