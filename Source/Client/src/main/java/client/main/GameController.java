package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.MapHalfService;
import client.movement.MovementService;
import client.utility.MyPlayerState;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromserver.EPlayerGameState;


public class GameController {

	private final static Logger logger = LoggerFactory.getLogger(GameController.class);

	private static final int SLEEP_TIME = 400;

	private int loopCount = 0;
	
	private boolean finished = false;
	private boolean sentMap;
	private GameModel gameModel;
	private boolean treasureCollected = false;
	private UtilityModel utilityModel;
	private MovementService movementService;
	private MapHalfService mapHalfSerivce;

	/**
	 * 
	 * Executes the main loop of the game
	 * Instantiates the GameStateManager and the CliController 
	 * 
	 * @param network
	 */
	public GameController(GameModel gameModel, UtilityModel utilityModel, MovementService movementService, MapHalfService mapHalfSerivce) {
		this.gameModel = gameModel;
		this.utilityModel = utilityModel;		
		this.movementService = movementService;
		this.mapHalfSerivce = mapHalfSerivce;
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
			gameModel.update();
			logger.info("Updated my player state");
			
			MyPlayerState myPlayerState = gameModel.getMyPlayerState();
			
			if (!treasureCollected && myPlayerState.hasTreasure()) {
				utilityModel.setTreasureCollected();
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
		utilityModel.setWaiting(EPlayerGameState.MustWait);
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
				gameModel.initializeProbabilityMap();
				gameModel.initialiseFindPath();
			}
			
			logger.info("Preparing path finding");
			
			//Need to update the terrain map each turn, since the client else moves towards water
			gameModel.getFindPath().updateTerrainMap(gameModel.getTerrainMap());
			sendMove();

			loopCount++;

		}
	}

	/**
	 * Presents loss via model and view
	 */
	private void presentLoss() {
		utilityModel.setLost(false, loopCount, gameModel.getMyPlayerState().hasTreasure());
		this.finished = true;
	}

	/**
	 * Presents win via model and view
	 */
	private void presentWin() {
		utilityModel.setWon(true, loopCount, gameModel.getMyPlayerState().hasTreasure());
		this.finished = true;
	}

	/**
	 * Gets the next move from FindPath via getting the movementContext and the permanent 
	 * find path instance held in the gameStateManager
	 * Sends the next move over the network
	 */
	private void sendMove() {
		logger.info("The AI will now determine which moves to make to win this game.");
	
		this.movementService.calculateAndSendMove(gameModel.getMovementContext(), gameModel.getFindPath());
		EMove move = movementService.getLastMove();
		utilityModel.setMove(move);
		
	}

	/**
	 * Calls MainHalfMapSending that sends the HalfMap
	 */
	private void sendHalfMap() {
		logger.info("We will now begin by creating a new map...");
		
		utilityModel.setMapCreation();
		
		this.mapHalfSerivce.generateAndSendMap(
				gameModel.getGameState().getMap(),
				result -> {
					if (!result.getIsValidMap()) {
						utilityModel.setMapValidationError(result.getResult());
					}
				}
		);
		
		utilityModel.setSentMap(true);
	}

	public boolean getFinished() {
		return finished;
	}
}
