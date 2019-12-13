package com.mbuyukasik.assignment.kalah.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbuyukasik.assignment.kalah.dao.entity.GameEntity;

/** 
 * GameRepository operates on GAME database table
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {

	List<GameEntity> findByCode(String code);
	
}
