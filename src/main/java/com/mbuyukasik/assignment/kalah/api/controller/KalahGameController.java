package com.mbuyukasik.assignment.kalah.api.controller;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mbuyukasik.assignment.kalah.api.model.CreateGameResponse;
import com.mbuyukasik.assignment.kalah.api.model.ErrorResponse;
import com.mbuyukasik.assignment.kalah.api.model.MoveResponse;
import com.mbuyukasik.assignment.kalah.exception.OperationResultException;
import com.mbuyukasik.assignment.kalah.model.operationresult.OperationResult;
import com.mbuyukasik.assignment.kalah.model.operationresult.ValueOperationResult;
import com.mbuyukasik.assignment.kalah.service.GameManager;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Main Rest Controller of Kalah Game
 * 
 * @author TTMBUYUKASIK
 *
 */
@RestController
@RequestMapping(value = "/games", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class KalahGameController {

	private static final String GAME_URL_TEMPLATE = "%s://%s:%d/games/%s";
	private static final String MOVE_URL_TEMPLATE = "%s://%s:%d/games/%s/pits/%d";

	private static final String CREATE_GAME_DESCRIPTION_FOR_201 = "Game successfully created";
	private static final String CREATE_GAME_DESCRIPTION_FOR_400 = "Game code is invalid!\n * Error Code-002, Error message: Game is not found";
	private static final String DESCRIPTION_FOR_500 = "Game successfully created";

	private static final String MOVE_DESCRIPTION_FOR_200 = "Move operation completed successfully";
	private static final String MOVE_DESCRIPTION_FOR_400 = "Invalid parameters. "
			+ " See details in response object! " 
			+ "\n* Error Code-003, Error message: Game is over"
			+ "\n* Error Code-004, Error message: Invalid pit id"
			+ "\n* Error Code-005, Error message: Pit is empty";

	@Autowired
	private GameManager gameManager;

	@ApiOperation(value = "This rest method is used to create a new game")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = CREATE_GAME_DESCRIPTION_FOR_201, response = CreateGameResponse.class),
			@ApiResponse(code = 400, message = CREATE_GAME_DESCRIPTION_FOR_400, response = ErrorResponse.class),
			@ApiResponse(code = 500, message = DESCRIPTION_FOR_500, response = ErrorResponse.class) })
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "")
	public ResponseEntity<CreateGameResponse> createGame(HttpServletRequest request) {
		ValueOperationResult<String> createResult = this.gameManager.createGame();
		if (OperationResult.isResultSucces(createResult)) {
			String gameCode = createResult.getResultValue();

			String gameUrl = String.format(GAME_URL_TEMPLATE, request.getScheme(), request.getServerName(),
					request.getServerPort(), gameCode);

			CreateGameResponse response = new CreateGameResponse(gameCode, gameUrl);

			return new ResponseEntity<CreateGameResponse>(response, HttpStatus.CREATED);
		} else {
			throw new OperationResultException(createResult, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "This method is used to receive player's move")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = MOVE_DESCRIPTION_FOR_200, response = MoveResponse.class),
			@ApiResponse(code = 400, message = MOVE_DESCRIPTION_FOR_400, response = ErrorResponse.class),
			@ApiResponse(code = 500, message = DESCRIPTION_FOR_500, response = ErrorResponse.class) })
	@ResponseStatus(HttpStatus.OK)
	@PutMapping(path = "/{game_code}/pits/{pit_id}")
	public ResponseEntity<MoveResponse> move(HttpServletRequest request,
			@PathVariable(name = "game_code") final String gameCode, @PathVariable(name = "pit_id") final int pitId) {
		// Operation is success. In other cases move method throws exception
		ValueOperationResult<int[]> moveResult = this.gameManager.move(gameCode, pitId);
		int[] gameStatus = moveResult.getResultValue();

		Comparator<String> intComp = new Comparator<String>() {
			@Override
			public int compare(String value1, String value2) {
				return Integer.valueOf(value1).compareTo(Integer.valueOf(value2)) ;
			}
		};
		
		Map<String, String> gameStatusMap = new TreeMap<String, String>(intComp);
		IntStream.range(0, gameStatus.length).forEach(index -> gameStatusMap.put(String.valueOf(index + 1), String.valueOf(gameStatus[index])));

		String moveUrl = String.format(MOVE_URL_TEMPLATE, request.getScheme(), request.getServerName(),
				request.getServerPort(), gameCode, pitId);

		return new ResponseEntity<MoveResponse>(new MoveResponse(gameCode, moveUrl, gameStatusMap), HttpStatus.OK);
	}

}
