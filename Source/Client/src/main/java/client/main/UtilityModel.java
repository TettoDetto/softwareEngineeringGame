package client.main;

import client.map.TerrainMap;
import client.map.validation.EValidationResults;
import client.utility.events.EPropertyChangeEventType;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.PropertyChangeSupport;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromserver.EPlayerGameState;

public class UtilityModel {

	private final PropertyChangeSupport changes;
	private boolean finished = false;
	private boolean sentMap;
	private TerrainMap terrainMap;
	private EPlayerGameState isWaiting = EPlayerGameState.MustAct;
	
	/**
	 * This Model class is used to store the states of miscellaneous things like if the map has been sent, if there is an error in map validation, etc.
	 * It fires any changes made in these aspects to notify any listening views. 
	 * This class exists to debloat the GameModel and keep things separate.
	 */
	public UtilityModel() {
		this.changes = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
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

	public void setWaiting(EPlayerGameState waiting) {
		EPlayerGameState before = this.isWaiting;
		this.isWaiting = waiting;
		changes.firePropertyChange(EPropertyChangeEventType.WAITING_FOR_OPPONENT, before, waiting);
	}

	public void setMapCreation() {
		changes.firePropertyChange(EPropertyChangeEventType.MAP_CREATION_STARTED, false, true);
		
	}

	public void setGameStart() {
		changes.firePropertyChange(EPropertyChangeEventType.GAME_STARTED, false, true);
		
	}

	public void setTreasureCollected() {
		changes.firePropertyChange(EPropertyChangeEventType.TREASURE_COLLECTED, false, true);
		
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
