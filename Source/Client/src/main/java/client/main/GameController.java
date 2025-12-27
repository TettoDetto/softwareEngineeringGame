package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.cli.CliModel;
import client.map.MapHalfSendingService;
import client.movement.StrategySwitcher;
import client.network.Network;
import client.network.data.PlayerMoveRequest;
import client.utility.MyPlayerState;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromserver.EPlayerGameState;


public class GameController {

	private final static Logger logger = LoggerFactory.getLogger(GameController.class);

	private static final int SLEEP_TIME = 400;

	private int loopCount = 0;
	
	private Network network;
	private boolean finished = false;
	private boolean sentMap;
	private GameStateManager gameStateManager;
	private boolean treasureCollected = false;
	private CliModel model;


	/**
	 * 
	 * Executes the main loop of the game
	 * Instantiates the GameStateManager and the CliController 
	 * 
	 * @param network
	 */
	public GameController(Network network, GameStateManager gameStateManager, CliModel model) {
		this.network = network;
		this.gameStateManager = gameStateManager;
		this.model = model;
	}

	/**
	 * 
	 * Keeps track of the game state and switches between waiting and handling the action
	 * Updates the GameStateManager to hold current information on visited, discovered, 
	 * TerrainMap and probabilityMap
	 *  
	 */
	public void executeGame() {
		try {
			Thread.sleep(SLEEP_TIME);
			gameStateManager.update();
			logger.info("Updated my player state");
			
			MyPlayerState myPlayerState = gameStateManager.getMyPlayerState();
			if (!treasureCollected && myPlayerState.hasTreasure()) {
				model.setTreasureCollected();
				treasureCollected = true;
			}

			switch (myPlayerState.getPlayerState()) {
			case EPlayerGameState.MustAct: {
				handleMustAct();
				break;
			}
			case EPlayerGameState.Won: {
				presentWin();
				logger.info("Loop count: " + loopCount);
				break;
			}
			case EPlayerGameState.Lost: {
				logger.info("Loop count: " + loopCount);
				presentLoss();
				break;
			}

			default:
				handleWaiting();

		}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}

	/**
	 * 
	 * Waits until it's the players move again
	 * 
	 * @throws InterruptedException
	 */
	private void handleWaiting() throws InterruptedException {
		model.setWaiting(EPlayerGameState.MustWait);
	}

	/**
	 * 
	 * Either calls map sending or movement sending
	 * Initializes a permanent probability map and find path instance
	 * 
	 */
	private void handleMustAct() {
		
		if (!sentMap) {
			
			sendHalfMap();
			sentMap = true;
			
			logger.info("Loop count after sending half map: " + loopCount);
			
		} else {
			
			if (loopCount == 0) {
				gameStateManager.initializeProbabilityMap();
				gameStateManager.initialiseFindPath();
			}
			
			logger.info("Preparing path finding");
			
			//Need to update the terrain map each turn, since the client else moves towards water
			gameStateManager.getFindPath().updateTerrainMap(gameStateManager.getTerrainMap());
			sendMove();

			loopCount++;

		}
	}

	/**
	 * Presents loss via model and view
	 */
	private void presentLoss() {
		model.setLost(false, loopCount, gameStateManager.getMyPlayerState().hasTreasure());
		this.finished = true;
	}

	/**
	 * Presents win via model and view
	 */
	private void presentWin() {
		model.setWon(true, loopCount, gameStateManager.getMyPlayerState().hasTreasure());
		this.finished = true;
	}

	/**
	 * Gets the next move from FindPath via getting the movementContext and the permanent 
	 * find path instance held in the gameStateManager
	 * Sends the next move over the network
	 */
	private void sendMove() {
		logger.info("The AI will now determine which moves to make to win this game.");
		
		EMove move = new StrategySwitcher().getMove(gameStateManager.getMovementContext(), gameStateManager.getFindPath());
		
		if (move != null) {
			logger.info("Moving towards: " + move);
			
			network.sendPlayerMove(new PlayerMoveRequest(gameStateManager.getMyPlayerState().getPlayerId(), move));
		}
		model.setMove(move);
		
	}

	/**
	 * Calls MainHalfMapSending that sends the HalfMap
	 */
	private void sendHalfMap() {
		logger.info("We will now begin by creating a new map...");
		
		model.setMapCreation();
		
		MapHalfSendingService halfMapSendingService = new MapHalfSendingService(
				network, 
				gameStateManager.getGameState().getMap(),
				result -> {
					if (!result.getIsValidMap()) {
						model.setMapValidationError(result.getResult());
					}
				}
		);
		
		halfMapSendingService.generateAndSendMap();
		model.setSentMap(true);
	}

	public boolean getFinished() {
		return finished;
	}
}
