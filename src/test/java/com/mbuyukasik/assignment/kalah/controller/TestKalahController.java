package com.mbuyukasik.assignment.kalah.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mbuyukasik.assignment.kalah.TestAbstractBootApplication;
import com.mbuyukasik.assignment.kalah.api.model.CreateGameResponse;
import com.mbuyukasik.assignment.kalah.api.model.ErrorResponse;
import com.mbuyukasik.assignment.kalah.api.model.MoveResponse;
import com.mbuyukasik.assignment.kalah.dao.entity.GameEntity;
import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;
import com.mbuyukasik.assignment.kalah.service.GameManager;
import com.mbuyukasik.assignment.kalah.service.game.GameService;
import com.mbuyukasik.assignment.kalah.util.RandomUtil;

/**
 * All cases of Kalah Game and rules are tested in this class
 * 
 * @author TTMBUYUKASIK
 *
 */
public class TestKalahController extends TestAbstractBootApplication {

	private static String CREATE_GAME_URL = "http://localhost:8080/games";
	private static String PIT_SUB_URL_FORMAT = "/pits/%d";
	
	private static String STATUS_FAIL_MESSAGE = "Incorrect status code!";

	@Value("${NUMBER_OF_STONES}")
	private int numberOfStones;

	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameManager gameManager;

	@Override
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testCreateGame() throws Exception {
		System.out.println("Create Game test started...");
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(TestKalahController.CREATE_GAME_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();

		String errorMessage = String.format("Status code incorrect. Expected: %d, received: %d !",
				HttpStatus.CREATED.value(), status);

		assertEquals(HttpStatus.CREATED.value(), status, errorMessage);
		String content = mvcResult.getResponse().getContentAsString();

		CreateGameResponse createGameResponse = super.mapFromJson(content, CreateGameResponse.class);

		assertNotNull(createGameResponse.getId(), "Game id is null in response of create game request!");
		assertNotNull(createGameResponse.getUrl(), "Game url is null in response of create game request!");

		System.out.println("Create Game test finished...");
	}

	@Test
	public void testMoveInvalidGameId() throws Exception {
		System.out.println("Move test with incorrect game id started...");

		CreateGameResponse createGameResponse = this.createGame();

		String moveUrl = String.format("%s%s", createGameResponse.getUrl(), PIT_SUB_URL_FORMAT);
		String url = moveUrl.replace(createGameResponse.getId(), "12345");
		url = String.format(url, 1);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		String errorMessage = String.format("Status code incorrect. Expected: %d, received: %d !",
				HttpStatus.BAD_REQUEST.value(), status);

		assertEquals(HttpStatus.BAD_REQUEST.value(), status, errorMessage);
		String content = mvcResult.getResponse().getContentAsString();

		ErrorResponse errorResponse = super.mapFromJson(content, ErrorResponse.class);

		errorMessage = String.format("Error code incorrect. Expected: %s, received: %s !",
				EnmResultCode.GAME_NOT_FOUNT.getCode(), errorResponse.getCode());

		assertEquals(errorResponse.getCode(), EnmResultCode.GAME_NOT_FOUNT.getCode(), errorMessage);
		System.out.println("Move test with incorrect game id finished...");
	}

	/**
	 * Move test with invalid pit id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMoveInvalidPitId() throws Exception {
		System.out.println("Move test with invalid pit id started...");

		// Create game first
		CreateGameResponse createGameResponse = this.createGame();
		String moveUrl = String.format("%s%s", createGameResponse.getUrl(), PIT_SUB_URL_FORMAT);

		// Make first move with invalid pit id (Out of bounds)
		String url = String.format(moveUrl, (numberOfStones * 2) + 3);

		String moveFailMessage = "Error code is incorrect.";
		this.testMove(url, HttpStatus.BAD_REQUEST, STATUS_FAIL_MESSAGE, EnmResultCode.INVALID_PIT, moveFailMessage);

		// 2. Kalah pit id is sent
		url = String.format(moveUrl, 7);

		this.testMove(url, HttpStatus.BAD_REQUEST, STATUS_FAIL_MESSAGE, EnmResultCode.INVALID_PIT, moveFailMessage);

		System.out.println("Move test with invalid pit id finished...");
	}

	/**
	 * Test Cases 1. Move test with valid pit id. Received status of game is also
	 * checked 2. Last stone is in kalah, Player should keep turn 3. Last stone is
	 * not in kalah, turn should swith to next player
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMoveValidPitId() throws Exception {
		System.out.println("Move test with valid pit id started...");

		int pitIndex = 1;

		CreateGameResponse createGameResponse = this.createGame();
		String moveUrl = String.format("%s%s", createGameResponse.getUrl(), PIT_SUB_URL_FORMAT);

		String url = String.format(moveUrl, pitIndex);

		MoveResponse moveResponse = this.testMove(url, HttpStatus.OK, STATUS_FAIL_MESSAGE, 
				null, null);

		if (moveResponse != null) {
			// Index of array
			pitIndex--;

			/*
			 * Expected status is dynamically prepared. Because number of stone can be
			 * modified to a value other than 6.
			 */
			int[] pitStatus = IntStream.range(1, (numberOfStones * 2) + 3).map(value -> numberOfStones).toArray();
			pitStatus[numberOfStones] = 0;
			pitStatus[pitStatus.length - 1] = 0;

			int lastStoneIndex = pitIndex + numberOfStones;
			IntStream.range(pitIndex + 1, lastStoneIndex + 1).forEach(index -> pitStatus[index]++);
			pitStatus[pitIndex] = 0;
			
			int[] receivedStatus = moveResponse.getStatus().values().stream().mapToInt(value -> Integer.valueOf(value)).toArray();

			String errorMessage = String.format("Game status is incorrect.!");
			assertArrayEquals(receivedStatus, pitStatus, errorMessage);

			GameEntity gameEntity = this.gameService.findByCode(createGameResponse.getId());

			assertEquals(gameEntity.getPlayer1Id(), gameEntity.getNextPlayerId(),
					"Last stone is in kalah, player should keep the turn.");

			// After this move player turn should switch to player 2
			url = String.format(moveUrl, 2);

			moveResponse = this.testMove(url, HttpStatus.OK, STATUS_FAIL_MESSAGE, 
					null, null);

			if (moveResponse != null) {
				gameEntity = this.gameService.findByCode(createGameResponse.getId());

				assertEquals(gameEntity.getPlayer2Id(), gameEntity.getNextPlayerId(),
						"Last stone is not in kalah, player turn should switch to the next.");
			}
		}
		System.out.println("Move test with valid pit id finished...");
	}

	/**
	 * Game Over test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGameOver() throws Exception {
		System.out.println("Gave over test is started...");

		CreateGameResponse createGameResponse = this.createGame();
		String moveUrl = String.format("%s%s", createGameResponse.getUrl(), PIT_SUB_URL_FORMAT);

		// modify game record to end game in next move
		int testPitIndex = numberOfStones - 1;
		GameEntity gameEntity = this.gameService.findByCode(createGameResponse.getId());
		
		int[] pitStatus = super.mapFromJson(gameEntity.getPitStatus(), int[].class);
		IntStream.range(0, testPitIndex).forEach(index->pitStatus[index] = 0);
		pitStatus[testPitIndex] = 1;
		
		String strUpdatedStatus = super.mapToJson(pitStatus);
		gameEntity.setPitStatus(strUpdatedStatus);
		this.gameService.save(gameEntity);
		
		String url = String.format(moveUrl, testPitIndex + 1);

		MoveResponse moveResponse = this.testMove(url, HttpStatus.OK, STATUS_FAIL_MESSAGE, 
				null, null);
		if (moveResponse != null) {
			
			moveResponse = this.testMove(url, HttpStatus.BAD_REQUEST, STATUS_FAIL_MESSAGE, 
					EnmResultCode.GAME_OVER, "Game is over, no move is allowed!");
		} else {
			System.out.println("Move that ends game is failed");
		}
		
		System.out.println("Game over test is finished...");
	}
	
	/**
	 * Game Over test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRandomGame() throws Exception {		
		System.out.println("Random game playing started...");

		CreateGameResponse createGameResponse = this.createGame();
		String moveUrl = String.format("%s%s", createGameResponse.getUrl(), PIT_SUB_URL_FORMAT);

		GameEntity gameEntity = this.gameService.findByCode(createGameResponse.getId());
		int[] pitStatus = super.mapFromJson(gameEntity.getPitStatus(), int[].class);
		
		boolean isGameOver = false;
		while (!isGameOver) {
			int startIndex = 0;
			int endIndex = 0;
			if (gameEntity.getNextPlayerId().equals(gameEntity.getPlayer1Id())) {
				startIndex = 0;
				endIndex = (pitStatus.length / 2) - 2;
			} else {
				startIndex = (pitStatus.length / 2);
				endIndex = pitStatus.length - 2;
			}
			int pitIndex = RandomUtil.generateRandomIndex(startIndex, endIndex);
			while (pitStatus[pitIndex] == 0) {
				pitIndex = RandomUtil.generateRandomIndex(startIndex, endIndex);
			}
			
			String url = String.format(moveUrl, pitIndex + 1);
			
			//System.out.println(String.format("Player %d moving pit index %d", gameEntity.getNextPlayerId(), pitIndex));
			MoveResponse moveResponse = this.testMove(url, HttpStatus.OK, STATUS_FAIL_MESSAGE, null, null);

			if (moveResponse == null) {
				System.out.println(String.format("Move is failed. pitId=%d", pitIndex + 1));
				break;
			}
			
			gameEntity = this.gameService.findByCode(createGameResponse.getId());
			pitStatus = super.mapFromJson(gameEntity.getPitStatus(), int[].class);
			
			isGameOver = this.gameManager.isGameOver(pitStatus);
		}
						
		System.out.println("Random game playing finished...");
		
	}

	private CreateGameResponse createGame() throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(TestKalahController.CREATE_GAME_URL)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();

		if (status == HttpStatus.CREATED.value()) {
			String content = mvcResult.getResponse().getContentAsString();

			return super.mapFromJson(content, CreateGameResponse.class);
		} else {
			throw new Exception("Game creation failed");
		}
	}

	private MoveResponse testMove(String url, HttpStatus successHttpStatus, String statusFailMessage,
			EnmResultCode expectedResultCode, String failMessage) throws Exception {
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		String errorMessage = String.format(statusFailMessage, successHttpStatus, status);
		assertEquals(successHttpStatus.value(), status, errorMessage);

		String content = mvcResult.getResponse().getContentAsString();

		if (successHttpStatus.value() == status && expectedResultCode == null) {
			return super.mapFromJson(content, MoveResponse.class);
		} else {
			ErrorResponse errorResponse = super.mapFromJson(content, ErrorResponse.class);
			if (expectedResultCode != null) {
				assertEquals(errorResponse.getCode(), expectedResultCode.getCode(), failMessage);
			} else {
				System.out.println(String.format("Error response is received unexpectedly. Error Code: %s, Error Message: %s", 
						errorResponse.getCode(), errorResponse.getMessage()));
			}
			return null;
		}
	}
	
}
