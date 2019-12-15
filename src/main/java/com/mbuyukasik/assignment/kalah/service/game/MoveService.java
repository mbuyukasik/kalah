package com.mbuyukasik.assignment.kalah.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbuyukasik.assignment.kalah.dao.entity.MoveEntity;
import com.mbuyukasik.assignment.kalah.dao.repository.MoveRepository;
import com.mbuyukasik.assignment.kalah.service.BaseService;

/**
 * MoveService is the service class for Move Entity
 * 
 * @author: mehmet buyukasik
 * @version 1.0
 */
@Service
public class MoveService extends BaseService {

	private MoveRepository moveRepository;

	@Autowired
	public MoveService(MoveRepository moveRepository) {
		this.moveRepository = moveRepository;
	}

	public void save(MoveEntity moveEntity) {
		this.moveRepository.save(moveEntity);
	}

	public MoveEntity findByGameIdAndSeq(Long gameId, Integer seq) {
		List<MoveEntity> moveEntityList = this.moveRepository.findByGameIdAndSeq(gameId, seq);
		if (moveEntityList != null && moveEntityList.size() > 0) {
			return moveEntityList.get(0);
		} else {
			return null;
		}
	}

}
