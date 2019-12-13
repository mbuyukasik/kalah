package com.mbuyukasik.assignment.kalah.service;

import java.util.List;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbuyukasik.assignment.kalah.dao.entity.GameEntity;
import com.mbuyukasik.assignment.kalah.dao.entity.MoveEntity;
import com.mbuyukasik.assignment.kalah.dao.entity.PlayerEntity;
import com.mbuyukasik.assignment.kalah.enumeration.EnmGameStatus;
import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;
import com.mbuyukasik.assignment.kalah.exception.OperationResultException;
import com.mbuyukasik.assignment.kalah.model.operationresult.OperationResult;
import com.mbuyukasik.assignment.kalah.model.operationresult.ValueOperationResult;
import com.mbuyukasik.assignment.kalah.service.game.GameService;
import com.mbuyukasik.assignment.kalah.service.game.MoveService;
import com.mbuyukasik.assignment.kalah.service.game.PlayerService;
import com.mbuyukasik.assignment.kalah.util.RandomUtil;

@Service
public class GameManager extends BaseService {

	private static Logger LOG = LoggerFactory.getLogger(GameManager.class);

	@Value("${NUMBER_OF_STONES}")
	private int numberOfStones;

	@Value("${GAME_CODE_LENGTH}")
	private int codeLength;

	private ObjectMapper objectMaapper;

	public GameManager() {
		this.objectMaapper = new ObjectMapper();
	}

	@Autowired
	private GameService gameService;
	@Autowired
	private MoveService moveService;
	@Autowired
	private PlayerService playerService;

	/**
	 * Method creates game and inserts in GAME table
	 * 
	 * @return
	 */
	public ValueOperationResult<String> createGame() {

		ValueOperationResult<String> operationResult = new ValueOperationResult<String>();
		try {
			/*
			 * Id of players should be taken as parameter on rest service. Current API
			 * design does not support it
			 */
			List<PlayerEntity> playerList = this.playerService.listAll();
			Long player1Id = playerList.get(0).getId();
			Long player2Id = playerList.get(1).getId();

			String gameCode = RandomUtil.genarateCode(codeLength);
			int[] pitStatus = IntStream.range(1, (numberOfStones * 2) + 3).map(value -> numberOfStones).toArray();

			pitStatus[numberOfStones] = 0;
			pitStatus[pitStatus.length - 1] = 0;

			GameEntity gameEntity = new GameEntity();
			gameEntity.setCode(gameCode);
			gameEntity.setNumberOfStones(numberOfStones);
			gameEntity.setLastMoveSeq(0);
			gameEntity.setPlayer1Id(player1Id);
			gameEntity.setPlayer2Id(player2Id);
			gameEntity.setNextPlayerId(player1Id);
			gameEntity.setStatus(EnmGameStatus.CREATED.getId());
			gameEntity.setPitStatus(objectMaapper.writeValueAsString(pitStatus));

			this.gameService.save(gameEntity);

			operationResult.setResultCode(EnmResultCode.SUCCESS);
			operationResult.setResultValue(gameCode);
		} catch (JsonProcessingException e) {
			LOG.error(e.getMessage(), e);
			operationResult.setResultCode(EnmResultCode.EXCEPTION);
			operationResult.setResultDesc(e.getMessage());
		}

		return operationResult;
	}

	/**
	 * 
	 * @param gameCode - Code of the game
	 * @param pitId    - Id of the pit
	 * @return
	 */
	@Transactional
	public ValueOperationResult<int[]> move(String gameCode, int pitId) {
		ValueOperationResult<int[]> result = null;
		try {
			GameEntity gameEntity = this.gameService.findByCode(gameCode);

			if (gameEntity != null) {

				// If validation success (In other cases exception is thrown)
				this.validateMove(gameEntity, pitId);

				Long currentPlayerId = gameEntity.getNextPlayerId();
				int pitIndex = pitId - 1;

				int[] status = objectMaapper.readValue(gameEntity.getPitStatus(), int[].class);

				int currentPlayerKalahIndex = 0;
				int player1KalahIndex = (status.length / 2) - 1;
				int player2KalahIndex = status.length - 1;

				if (pitIndex < (status.length / 2)) {
					currentPlayerKalahIndex = (status.length / 2) - 1;
				} else {
					currentPlayerKalahIndex = status.length - 1;
				}

				// Step 1 : Distribute stones in pitIndex to following pits (starting from index
				// + 1)
				int lastStoneIndex = this.moveStonesInPit(status, pitIndex);
				// rule 1 : If last stone is in kalah, player will keep the turn. NextPlayerId
				// will not change
				if (lastStoneIndex != currentPlayerKalahIndex) {

					Long nextPlayerId = gameEntity.getNextPlayerId().equals(gameEntity.getPlayer1Id())
							? gameEntity.getPlayer2Id()
							: gameEntity.getPlayer1Id();
					gameEntity.setNextPlayerId(nextPlayerId);
				}

				// apply capture opposite pits rule
				this.applyCaptureOppsiteRule(status, pitIndex, lastStoneIndex);
				// check if the game is over
				boolean isGameOver = this.checkGameOver(status);
				if (isGameOver) {
					gameEntity.setPlayer1Scrore(status[player1KalahIndex]);
					gameEntity.setPlayer2Scrore(status[player2KalahIndex]);
					if (gameEntity.getPlayer1Scrore() > gameEntity.getPlayer2Scrore()) {
						gameEntity.setWinnerPlayerId(gameEntity.getPlayer1Id());
					} else if (gameEntity.getPlayer2Scrore() > gameEntity.getPlayer1Scrore()) {
						gameEntity.setWinnerPlayerId(gameEntity.getPlayer2Id());
					} else {
						// Equal scrores
						gameEntity.setWinnerPlayerId(0l);
					}
					gameEntity.setStatus(EnmGameStatus.OVER.getId());
				}

				gameEntity.setPitStatus(objectMaapper.writeValueAsString(status));
				gameEntity.setLastMoveSeq(gameEntity.getLastMoveSeq() + 1);

				this.gameService.save(gameEntity);

				MoveEntity moveEntity = new MoveEntity();
				moveEntity.setGameId(gameEntity.getId());
				moveEntity.setPlayerId(currentPlayerId);
				moveEntity.setPitId(pitIndex);
				moveEntity.setSeq(gameEntity.getLastMoveSeq() + 1);
				moveEntity.setPitStatus(gameEntity.getPitStatus());

				this.moveService.save(moveEntity);

				result = new ValueOperationResult<int[]>(EnmResultCode.SUCCESS, status);
			} else {
				String message = String.format("Game not found code: %s", gameCode);
				LOG.warn(message);
				result = new ValueOperationResult<int[]>(EnmResultCode.GAME_NOT_FOUNT, null);
				throw new OperationResultException(result, HttpStatus.BAD_REQUEST);
			}
		} catch (JsonProcessingException e) {
			result = new ValueOperationResult<int[]>(EnmResultCode.EXCEPTION, null);
		}

		if (OperationResult.isResultSucces(result)) {
			return result;
		} else {
			throw new OperationResultException(result, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 
	 * @param gameEntity - Game record in Game table
	 * @param pitId
	 * @return
	 */
	private OperationResult validateMove(GameEntity gameEntity, Integer pitId) {
		OperationResult result = null;
		try {
			int pitIndex = pitId - 1;
			if (!gameEntity.getStatus().equals(EnmGameStatus.OVER.getId())) {

				int[] status = objectMaapper.readValue(gameEntity.getPitStatus(), int[].class);

				int startIndex = 0;
				int endIndex = 0;

				if (gameEntity.getNextPlayerId().equals(gameEntity.getPlayer1Id())) {
					startIndex = 0;
					endIndex = (status.length / 2) - 2;
				} else {
					startIndex = (status.length / 2);
					endIndex = status.length - 1;
				}

				if (pitIndex >= startIndex && pitIndex <= endIndex) {
					if (status[pitIndex] > 0) {
						result = new OperationResult(EnmResultCode.SUCCESS, EnmResultCode.SUCCESS.getMessage());
					} else {
						result = new OperationResult(EnmResultCode.PIT_EMPTY, EnmResultCode.PIT_EMPTY.getMessage());
					}
				} else {
					result = new OperationResult(EnmResultCode.INVALID_PIT, EnmResultCode.INVALID_PIT.getMessage());
				}

			} else {
				result = new OperationResult(EnmResultCode.GAME_OVER, EnmResultCode.GAME_OVER.getMessage());
			}
		} catch (JsonProcessingException e) {
			result = new OperationResult(EnmResultCode.EXCEPTION, "Status of the game could not be desi");
		}

		if (OperationResult.isResultSucces(result)) {
			return result;
		} else {
			throw new OperationResultException(result, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Check if game is over. If True update status array
	 * 
	 * @param gameStatus
	 * @return
	 */
	public boolean checkGameOver(int[] gameStatus) {
		boolean isGameOver = false;

		int player1StartIndex = 0;
		int player1KalahIndex = (gameStatus.length / 2) - 1;

		int player2StartIndex = (gameStatus.length / 2);
		int player2KalahIndex = gameStatus.length - 1;

		int[] player1NonZeroIndexes = IntStream.range(player1StartIndex, player1KalahIndex)
				.filter(index -> gameStatus[index] > 0).toArray();

		if (player1NonZeroIndexes.length == 0) {
			isGameOver = true;
			// Player 2 's remaining stones are moved to player's kalah
			int player2Remaining = IntStream.range(player2StartIndex, player2KalahIndex).map(index -> gameStatus[index])
					.sum();
			gameStatus[player2KalahIndex] = gameStatus[player2KalahIndex] + player2Remaining;
			IntStream.range(player2StartIndex, player2KalahIndex).forEach(index -> gameStatus[index] = 0);
		} else {
			int[] player2NonZeroIndexes = IntStream.range(player2StartIndex, player2KalahIndex)
					.filter(index -> gameStatus[index] > 0).toArray();
			if (player2NonZeroIndexes.length == 0) {
				isGameOver = true;
				int player1Remaining = IntStream.range(player1StartIndex, player1KalahIndex)
						.map(index -> gameStatus[index]).sum();
				gameStatus[player1KalahIndex] = gameStatus[player1KalahIndex] + player1Remaining;
				IntStream.range(player1StartIndex, player1KalahIndex).forEach(index -> gameStatus[index] = 0);
			}
		}

		return isGameOver;
	}

	/**
	 * If last stone comes in an empty pit (not kalah), stones in opposite will be
	 * captured to player's kalah together with the last stone
	 * 
	 * @param gameStatus
	 * @return
	 */
	public void applyCaptureOppsiteRule(int[] gameStatus, int pitIndex, int lastStoneIndex) {
		/*
		 * rule 2: When the last stone lands in an own empty pit, the player captures
		 * this stone and all stones in the opposite pit (the other players' pit) and
		 * puts them in his own Kalah.
		 */
		int player1StartIndex = 0;
		int player1KalahIndex = (gameStatus.length / 2) - 1;

		int player2StartIndex = (gameStatus.length / 2);
		int player2KalahIndex = gameStatus.length - 1;

		int currentPlayerKalahIndex = 0;
		if (pitIndex < player1KalahIndex) {
			currentPlayerKalahIndex = player1KalahIndex;
		} else {
			currentPlayerKalahIndex = player2KalahIndex;
		}

		boolean apply = false;
		if ((isInRange(player1StartIndex, player1KalahIndex - 1, pitIndex)
				&& isInRange(player1StartIndex, player1KalahIndex - 1, lastStoneIndex)
				|| (isInRange(player2StartIndex, player2KalahIndex - 1, pitIndex)
						&& isInRange(player2StartIndex, player2KalahIndex - 1, lastStoneIndex)))) {
			apply = true;
		}
		if (apply && gameStatus[lastStoneIndex] == 1) {

			int distanceFromKalah = player1KalahIndex - lastStoneIndex;
			int oppositeIndex = player1KalahIndex + distanceFromKalah;

			gameStatus[currentPlayerKalahIndex] = gameStatus[currentPlayerKalahIndex] + gameStatus[lastStoneIndex]
					+ gameStatus[oppositeIndex];
			gameStatus[lastStoneIndex] = 0;
			gameStatus[oppositeIndex] = 0;
		}
	}

	/**
	 * Check if game is over.
	 * 
	 * @param gameStatus
	 * @return
	 */
	public boolean isGameOver(int[] gameStatus) {
		int player1StartIndex = 0;
		int player1KalahIndex = (gameStatus.length / 2) - 1;

		int player2StartIndex = (gameStatus.length / 2);
		int player2KalahIndex = gameStatus.length - 1;

		int[] player1NonZeroIndexes = IntStream.range(player1StartIndex, player1KalahIndex)
				.filter(index -> gameStatus[index] > 0).toArray();
		int[] player2NonZeroIndexes = IntStream.range(player2StartIndex, player2KalahIndex)
				.filter(index -> gameStatus[index] > 0).toArray();

		if (player1NonZeroIndexes.length == 0 || player2NonZeroIndexes.length == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Distribute stones in pitIndex to following pits (starting from index + 1)
	 * Kalah of opposite player's kalah should be skipped
	 * 
	 * @param gameStatus
	 * @return
	 */
	public int moveStonesInPit(int[] pitStatus, int pitIndex) {
		int opponentPlayerKalahIndex = 0;

		int player1KalahIndex = (pitStatus.length / 2) - 1;
		int player2KalahIndex = pitStatus.length - 1;

		if (pitIndex < player1KalahIndex) {
			opponentPlayerKalahIndex = player2KalahIndex;
		} else {
			opponentPlayerKalahIndex = player1KalahIndex;
		}

		int index = pitIndex + 1;
		int lastStoneIndex = -1;
		int stoneInPit = pitStatus[pitIndex];
		// This assignment is done here. Because after distribution of stones this index may include a stone.
		pitStatus[pitIndex] = 0;
		
		for (int i = 1; i <= stoneInPit; i++) {
			index = index % 14;
			pitStatus[index] = pitStatus[index] + 1;
			lastStoneIndex = index;

			index++;
			if (index == opponentPlayerKalahIndex) {
				// Opponent Player's kalah should be skipped
				index++;
			}
		}
		
		return lastStoneIndex;
	}

	/**
	 * 
	 * @param min   Inclusive
	 * @param max   Inclusive
	 * @param value
	 * @return
	 */
	private boolean isInRange(int min, int max, int value) {
		return (value >= min && value <= max);
	}

}
