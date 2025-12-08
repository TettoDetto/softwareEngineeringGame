package client.network;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import client.map.MapHalfGenerator;
import client.map.MapNode;
import client.utility.HalfMapDTO;
import client.utility.MapNodeDTO;
import client.utility.PlayerMoveRequest;
import client.utility.exceptions.GameStateEmptyException;
import client.utility.exceptions.InvalidMapHalfException;
import client.utility.exceptions.InvalidMoveException;
import client.utility.exceptions.NetworkErrorException;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;

public class Network implements INetwork {

	private final WebClient webClient;
	private final String gameId;
	private UniquePlayerIdentifier playerId;
	private final static Logger logger = LoggerFactory.getLogger(Network.class);

	/**
	 * Network CTOR, generates a global instance of the web client
	 * 
	 * @param serverBaseUrl
	 * @param gameId
	 */
	public Network(String serverBaseUrl, String gameId) {

		if (serverBaseUrl == "" || serverBaseUrl == " ") {
			throw new IllegalArgumentException("Server URL cannot be empty!");
		}
		if (gameId == "" || gameId == " ") {
			throw new IllegalArgumentException("Game ID cannot be empty!");
		}
		this.webClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

		logger.info("Web Client has been constructed");

		this.gameId = gameId;

	}

	/**
	 * Generates the unique player id
	 * 
	 * @param firstName
	 * @param lastName
	 * @param uAccount
	 * @return
	 */
	public UniquePlayerIdentifier registerPlayer(String firstName, String lastName, String uAccount) {

		PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, uAccount);

		logger.info("Player is being registrated...");

		Mono<ResponseEnvelope<UniquePlayerIdentifier>> webAccess = webClient.method(HttpMethod.POST)
				.uri("/" + gameId + "/players").body(BodyInserters.fromValue(playerReg)) // specify the data which is
																							// sent to the server
				.retrieve()
				// expected object type to be returned by the server
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<UniquePlayerIdentifier>>() {
				});

		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

		if (resultReg.getState() == ERequestState.Error) {
			System.err.println("🚨 Client error, received message: " + resultReg.getExceptionMessage());
		} else {
			this.playerId = resultReg.getData().get();
			logger.info("Player has been registrated with the following ID: {}", playerId.getUniquePlayerID());

		}
		return playerId;

	}

	/**
	 * Makes a GET Request for the current game state to the server
	 * 
	 * @return
	 * @throws Exception
	 */
	public GameState getGameState() throws NetworkErrorException, GameStateEmptyException {

		logger.info("Requesting game state...");

		Mono<ResponseEnvelope<GameState>> webAccess = webClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + playerId.getUniquePlayerID()).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<GameState>>() {
				});

		logger.info("Game state recieved");

		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			throw new NetworkErrorException("Game state request failed");
		}

		if (requestResult.getData().isEmpty()) {
			throw new GameStateEmptyException("The recieved game state contains no data");
		}

		else {
			GameState currentServerGameState = requestResult.getData().get();
			logger.info("Current Game State:  " + currentServerGameState.getPlayers());
			return currentServerGameState;
		}
	}

	/*
	 * TODO: Make PlayerMoveRequest Method, for a POST move that can be serialized
	 * to XML. Makes a POST request to the server, to send the player Move
	 * 
	 * @param move
	 * 
	 * @return
	 */

	public boolean sendPlayerMove(PlayerMoveRequest move) {
		Mono<ResponseEnvelope<Boolean>> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(move)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Boolean>>() {
				});

		logger.info("Sending player moves");

		ResponseEnvelope<Boolean> result = webAccess.block();

		if (result.getState() == ERequestState.Error) {
			logger.warn("The move hasn't been accepted due to: " + result.getExceptionMessage());
			throw new InvalidMoveException(
					"The move hasn't been accepted by the server due to: " + result.getExceptionMessage());
		} else {
			logger.info("The move has been acepted!");
			return true;
		}
	}

	public boolean sendMapHalf(MapHalfGenerator mapHalf) {

		logger.info("Converting the half map into a DTO");
		HalfMapDTO halfMapDTO = new HalfMapDTO();
		halfMapDTO.setPlayerId(playerId.getUniquePlayerID());
		halfMapDTO.setMapNodes(convertToMapNodeDTOs(mapHalf));

		logger.info("Conversion successful");
		logger.info("Sending Half map...");

		Mono<ResponseEnvelope<Boolean>> webAccess = webClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
				// .header("Content-Type", "application/xml")
				// .header("Accept", "application/xml")
				.body(BodyInserters.fromValue(halfMapDTO)).retrieve()
				.bodyToMono(new ParameterizedTypeReference<ResponseEnvelope<Boolean>>() {
				});

		logger.info("Sent half map! Waiting for server response...");

		ResponseEnvelope<Boolean> result = webAccess.block();

		if (result.getState() == ERequestState.Error) {
			logger.warn("The half map hasn't been accepted due to: " + result.getExceptionMessage());
			throw new InvalidMapHalfException("The map hasn't been accepted due to: " + result.getExceptionMessage());
		} else {
			logger.info("The half map has been accepted!");
			return true;
		}
	}

	private List<MapNodeDTO> convertToMapNodeDTOs(MapHalfGenerator mapHalf) {
		List<MapNodeDTO> nodeDTOs = new ArrayList<>();
		MapNode[][] mapNodes = mapHalf.getMap();

		for (int y = 0; y < mapNodes.length; y++) {
			for (int x = 0; x < mapNodes[y].length; x++) {
				MapNode node = mapNodes[y][x];
				// Convert Terrain enum to string value
				ETerrain terrain = node.getTerrain();

				nodeDTOs.add(new MapNodeDTO(node.getX(), node.getY(), terrain, node.hasCastle()));
			}
		}

		return nodeDTOs;
	}

	public UniquePlayerIdentifier getPlayerId() {
		return playerId;
	}
}
