package client.main;

import java.awt.Point;
import java.util.function.Predicate;

import client.map.TerrainMap;
import client.movement.FindPath;
import client.movement.IMovementContext;
import client.movement.MovementContext;
import client.movement.ProbabilityMap;
import client.network.Network;
import client.utility.MyPlayerState;
import client.utility.PlayerPosition;
import client.utility.exceptions.GameStateEmptyException;
import client.utility.exceptions.NetworkErrorException;
import messagesbase.messagesfromserver.GameState;

public class GameStateManager {

	private GameState gameState;
	private TerrainMap terrainMap;
	private MyPlayerState myPlayerState;
	private ProbabilityMap probMap;
	private Network network;
	private final MovementContext movementContext;
	private FindPath findPath;
	
	/**
	 * 
	 * Helper class for holding TerrainMap, GameState, PlayerState, ProbabilityMap, MovementContext and FindPath
	 * 
	 * @param network
	 */
	public GameStateManager(Network network) {
		this.network = network;
		this.movementContext = new MovementContext();
	}
	
	/**
	 * 
	 * Updates the states of TerrainMap, PlayerPosition and Treasure possession
	 * 
	 */
	public void update() {
		try {
			this.gameState = network.getGameState();
		} catch (NetworkErrorException e) {
			e.printStackTrace();
		} catch (GameStateEmptyException e) {
			e.printStackTrace();
		}
		this.terrainMap = new TerrainMap(gameState.getMap().getMapNodes());
		this.myPlayerState = new MyPlayerState(gameState, network.getPlayerId().getUniquePlayerID());
		
		movementContext.setPlayerPosition(new PlayerPosition(gameState.getMap()));
		movementContext.setTerrainMap(terrainMap);
		movementContext.setHasTreasure(myPlayerState.hasTreasure());
	}
	
	public IMovementContext getMovementContext() {
		return movementContext;
	}
	
	public TerrainMap getTerrainMap() {
		return terrainMap;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	/**
	 * 
	 * Initializes the ProbabilityMap once and passes it to movementContext
	 * 
	 */
	public void initializeProbabilityMap() {
		boolean splitVertically;
		if (terrainMap.getXDimension() > terrainMap.getYDimension()) {
			splitVertically = true;
		} else {
			splitVertically = false;
		}

		Predicate<Point> ownHalf = p -> splitVertically ? p.x < terrainMap.getXDimension() / 2
				: p.y < terrainMap.getYDimension() / 2;
		Predicate<Point> enemyHalf = ownHalf.negate();

		this.probMap = new ProbabilityMap(terrainMap, ownHalf, enemyHalf);
		movementContext.setProbabilityMap(probMap);
	}
	
	public MyPlayerState getMyPlayerState() {
		return myPlayerState;
	}
	
	/**
	 * 
	 * Generates a new instance of FindPath to be used during the whole game
	 * 
	 */
	public void initialiseFindPath() {
		this.findPath = new FindPath(movementContext);
	}

	public FindPath getFindPath() {
		return findPath;
	}
	
		
}
