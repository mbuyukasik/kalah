package com.mbuyukasik.assignment.kalah.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbuyukasik.assignment.kalah.dao.entity.GameEntity;
import com.mbuyukasik.assignment.kalah.dao.repository.GameRepository;
import com.mbuyukasik.assignment.kalah.service.BaseService;

/**
 * GameService is the service class for Game Entity
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Service
public class GameService extends BaseService {

	//private static Logger LOG = LoggerFactory.getLogger(GameService.class);

	private GameRepository gameRepository;

	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public void save(GameEntity gameEntity) {
		this.gameRepository.save(gameEntity);
	}
	
	public GameEntity findByCode(String code) {
		List<GameEntity> gameEntityList = this.gameRepository.findByCode(code);
		if (gameEntityList != null && gameEntityList.size() > 0) {
			return gameEntityList.get(0);
		} else {
			return null;
		}
	}

}
