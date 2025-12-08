package client.utility;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class MyPlayerState {
	final Logger logger = LoggerFactory.getLogger(MyPlayerState.class);
	private String playerId;
	private PlayerState player;

	/**
	 * Saves the current state of the player, of which the uniquePlayerId matches
	 * with the gameState.getPlayers() set
	 * 
	 * @param gameState
	 * @param playerId
	 */
	public MyPlayerState(GameState gameState, String playerId) {
		if (gameState == null || playerId == null) {
			throw new IllegalArgumentException("GameState or PlayerID cannot be null");
		}

		this.playerId = playerId;

		Set<PlayerState> playerState = gameState.getPlayers();

		for (PlayerState player : playerState) {
			if (player.getUniquePlayerID().equals(playerId)) {
				this.player = player;
				playerId = player.getUniquePlayerID();
				logger.info("Following Player {} has State: " + this.player, playerId);
				break;
			}

		}
	}

	public boolean hasTreasure() {
		return this.player.hasCollectedTreasure();
	}

	public EPlayerGameState getPlayerState() {
		return this.player.getState();
	}

	public MyPlayerState updateGameState(GameState gameState, String playerId) {
		return new MyPlayerState(gameState, playerId);
	}

	public String getPlayerId() {
		return this.playerId;
	}

}
