/*package client.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.main.GameResult;
import client.map.TerrainMap;
import client.utility.PlayerMoveRequest;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.IPropertyChangeObserver;
import client.utility.events.PropertyChangeSupport;
import client.utility.events.PropertyType;
import client.utility.events.PropertyTypes;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;

public class CliModel {
	private final static Logger logger = LoggerFactory.getLogger(CliModel.class);

	private final PropertyChangeSupport changes;
	private boolean finished = false;
	private GameState gameState = null;
	private boolean sentMap;
	private TerrainMap terrainMap;
	
	public CliModel() {
		this.changes = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}

	public <T> void addPropertyChangeObserver(PropertyType<T> type, IPropertyChangeObserver<T> observer) {
		changes.addPropertyChangeObserver(type, observer);
	}

	public <T> void removePropertyChangeObserver(PropertyType<T> type, IPropertyChangeObserver<T> observer) {
		changes.removePropertyChangeObserver(type, observer);
	}

	public void updateGameState(GameState gameState) {

		try {
			GameState newGameState = gameState;
			GameState oldGameState = this.gameState;
			this.gameState = newGameState;

			if (newGameState != null) {

				changes.firePropertyChange(PropertyTypes.GAME_STATE, oldGameState, newGameState);
			}
		} catch (Exception e) {
			changes.firePropertyChange(PropertyTypes.EXCEPTION, null, e);
		}

	}

	public GameState getGameState() {
		return this.gameState;
	}

	public void setSentMap(boolean value) {
		boolean newSentMap = value;
		boolean oldSentMap = this.sentMap;
		this.sentMap = newSentMap;
		changes.firePropertyChange(PropertyTypes.MAP_SENT, oldSentMap, newSentMap);
	}

	public void updateTerrainMap(TerrainMap newTerrainMap) {
		TerrainMap oldTerrainMap = this.terrainMap;
		this.terrainMap = newTerrainMap;
		changes.firePropertyChange(PropertyTypes.TERRAIN_MAP, oldTerrainMap, newTerrainMap);
	}

	public TerrainMap getTerrainMap() {
		return this.terrainMap;
	}


	public void setFinished(boolean hasWon, int loopCount, boolean hasTreasure) {
		this.finished = true;
		GameResult result = new GameResult(hasWon, loopCount, hasTreasure);
		if (hasWon) {
			logger.info("Client won after capturing the enemy castle");
		}
		else {
			logger.info("Client has lost after castle got caputred");
		}
		changes.firePropertyChange(PropertyTypes.GAME_FINISHED, null, result);
	}

	public boolean getFinished() {
		return finished;
	}

	public void setMove(PlayerMoveRequest move) {
		changes.firePropertyChange(PropertyTypes.MOVE_SENT, null, null);
	}

	public void setWaiting(boolean b) {
		changes.firePropertyChange(PropertyTypes.GAME_STATUS, null, EPlayerGameState.MustWait);

	}

}
*/