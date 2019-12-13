package com.mbuyukasik.assignment.kalah.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbuyukasik.assignment.kalah.dao.entity.PlayerEntity;

/** 
 * PlayerRepository operates on PLAYER database table
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

}
