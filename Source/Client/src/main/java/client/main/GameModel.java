package client.main;

import client.map.TerrainMap;
import client.movement.FindPath;
import client.movement.ProbabilityMap;
import client.movement.model.IMovementContext;
import client.movement.model.MovementContext;
import client.network.Network;
import client.utility.MyPlayerState;
import client.utility.PlayerPosition;
import client.utility.events.EPropertyChangeEventType;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.PropertyChangeSupport;
import client.utility.exceptions.GameStateEmptyException;
import client.utility.exceptions.NetworkErrorException;
import messagesbase.messagesfromserver.GameState;

public class GameModel {
	
	private final PropertyChangeSupport changes;
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
	public GameModel(Network network) {
		this.network = network;
		this.movementContext = new MovementContext();
		this.changes = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}
	
	/**
	 * 
	 * Updates the states of TerrainMap, PlayerPosition and Treasure possession
	 * 
	 */
	public void update() {
		try {
			GameState oldGameState = this.gameState;
			this.gameState = network.getGameState();
			changes.firePropertyChange(EPropertyChangeEventType.GAME_STATE, oldGameState, this.gameState);
			
		} catch (NetworkErrorException e) {
			e.printStackTrace();
		} catch (GameStateEmptyException e) {
			e.printStackTrace();
		}
		
		this.terrainMap = new TerrainMap(gameState.getMap().getMapNodes());
		changes.firePropertyChange(EPropertyChangeEventType.MAP_UPDATED, null, terrainMap);
		
		this.myPlayerState = new MyPlayerState(gameState, network.getPlayerId().getUniquePlayerID());
		
		movementContext.setPlayerPosition(new PlayerPosition(gameState.getMap()));		
		movementContext.setTerrainMap(terrainMap);
		movementContext.setHasTreasure(myPlayerState.hasTreasure());
	}
	
	/**
	 * 
	 * Initializes the ProbabilityMap once and passes it to movementContext
	 * 
	 */
	public void initializeProbabilityMap() {
		this.probMap = new ProbabilityMap(terrainMap);
		movementContext.setProbabilityMap(probMap);
	}
	
	/**
	 * 
	 * Generates a new instance of FindPath to be used during the whole game
	 * 
	 */
	public void initialiseFindPath() {
		this.findPath = new FindPath(movementContext);
	}
	
	public MyPlayerState getMyPlayerState() {
		return myPlayerState;
	}

	public FindPath getFindPath() {
		return findPath;
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
	
	
		
}
