package com.mbuyukasik.assignment.kalah.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbuyukasik.assignment.kalah.dao.entity.MoveEntity;

/** 
 * MoveRepository operates on MOVE database table
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Repository
public interface MoveRepository extends JpaRepository<MoveEntity, Long> {

	public List<MoveEntity> findByGameIdAndSeq(Long gameId, Integer seq);
	
}
