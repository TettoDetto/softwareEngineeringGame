package client.utility;

import java.awt.Point;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.utility.exceptions.InvalidPlayerPositionException;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

public class PlayerPosition implements IPlayerPosition {
	final Logger logger = LoggerFactory.getLogger(PlayerPosition.class);
	private int posX;
	private int posY;
	private FullMap map;
	private Optional<FullMapNode> previousPosition;
	private TerrainMap terrainMap;

	/**
	 * The current position of the player based on the FullMap given by the server
	 * 
	 * @param map
	 * @param terrainMap
	 */
	public PlayerPosition(FullMap map) {
		this.map = map;
	}

	/**
	 * Main function of the class, determines the current position by looping
	 * through the full map and checking if any field has MyPlayerPosition on it
	 * Saves that field as a node and as coordinates and if the player was on
	 * another node in before hand, it safes the previous positions. Useful for
	 * determining on which Node the client just was
	 */
	@Override
	public Point playerLocation() {
		for (FullMapNode node : map.getMapNodes()) {
			if (node.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition
					|| node.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition) {
				this.posX = node.getX();
				this.posY = node.getY();
				logger.info("Player position at: {}, {}", posX, posY);
				return new Point(node.getX(), node.getY());
			}
		}
		throw new InvalidPlayerPositionException("Player not found on the map");
	}

	@Override
	public void update(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

	public int getPlayerPositionX() {
		return posX;
	}

	public int getPlayerPositionY() {
		return posY;
	}

	public Optional<FullMapNode> getPreviousPosition() {
		return previousPosition;
	}

	public FullMapNode getCurrentPositionNode() {
		return terrainMap.getMapNode(posX, posY);
	}

}
