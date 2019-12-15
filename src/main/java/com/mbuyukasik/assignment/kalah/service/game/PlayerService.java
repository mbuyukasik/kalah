package com.mbuyukasik.assignment.kalah.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbuyukasik.assignment.kalah.dao.entity.PlayerEntity;
import com.mbuyukasik.assignment.kalah.dao.repository.PlayerRepository;
import com.mbuyukasik.assignment.kalah.service.BaseService;

/**
 * PlayerService is the service class for Player Entity
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Service
public class PlayerService extends BaseService {

	private PlayerRepository playerRepository;

	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public List<PlayerEntity> listAll() {
		return this.playerRepository.findAll();
	}

}
