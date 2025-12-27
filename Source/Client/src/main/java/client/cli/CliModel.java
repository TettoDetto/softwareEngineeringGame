package client.cli;

import client.main.GameResult;
import client.map.TerrainMap;
import client.map.validation.EValidationResults;
import client.utility.events.EPropertyChangeEventType;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.PropertyChangeSupport;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;

public class CliModel {

	private final PropertyChangeSupport changes;
	private boolean finished = false;
	private GameState gameState = null;
	private boolean sentMap;
	private TerrainMap terrainMap;
	private boolean treasure;
	
	public CliModel() {
		this.changes = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}

	public void updateGameState(GameState gameState) {

		GameState newGameState = gameState;
		GameState oldGameState = this.gameState;
		this.gameState = newGameState;

		changes.firePropertyChange(EPropertyChangeEventType.GAME_STATE, oldGameState, newGameState);
	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void setSentMap(boolean value) {
		boolean newSentMap = value;
		boolean oldSentMap = this.sentMap;
		this.sentMap = newSentMap;
		changes.firePropertyChange(EPropertyChangeEventType.MAP_SENT_SUCCESSFULLY, oldSentMap, newSentMap);
	}
	
	public void setMapValidationError(EValidationResults eValidationResults) {
		changes.firePropertyChange(EPropertyChangeEventType.MAP_VALIDATION_ERROR, null, eValidationResults);
	}

	public void updateTerrainMap(TerrainMap newTerrainMap) {
		TerrainMap oldTerrainMap = this.terrainMap;
		this.terrainMap = newTerrainMap;
		changes.firePropertyChange(EPropertyChangeEventType.MAP_UPDATED, oldTerrainMap, newTerrainMap);
	}

	public TerrainMap getTerrainMap() {
		return this.terrainMap;
	}

	public boolean getFinished() {
		return finished;
	}

	public void setMove(EMove move) {
		changes.firePropertyChange(EPropertyChangeEventType.MOVE_CALCULATED, null, move);
	}

	public void setWaiting(boolean b) {
		changes.firePropertyChange(EPropertyChangeEventType.WAITING_FOR_OPPONENT, null, EPlayerGameState.MustWait);

	}

	public void setMapCreation() {
		changes.firePropertyChange(EPropertyChangeEventType.MAP_CREATION_STARTED, null, "");
		
	}

	public void setGameStart() {
		changes.firePropertyChange(EPropertyChangeEventType.GAME_STARTED, null, "");
		
	}

	public void setTreasureCollected() {
		this.treasure = true;
		changes.firePropertyChange(EPropertyChangeEventType.TREASURE_COLLECTED, null, "");
		
	}

	public void setWon(boolean won, int loopCount, boolean hasTreasure) {
		this.finished = true;
		GameResult result = new GameResult(won, loopCount, hasTreasure);
		changes.firePropertyChange(EPropertyChangeEventType.GAME_WON, null, result);
	}
	public void setLost(boolean won, int loopCount, boolean hasTreasure) {
		this.finished = true;
		GameResult result = new GameResult(won, loopCount, hasTreasure);
		changes.firePropertyChange(EPropertyChangeEventType.GAME_LOST, null, result);
	}

}
