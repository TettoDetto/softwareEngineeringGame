package client.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.ETerrain;

public class MapNode {
	private boolean hasCastle;
	private ETerrain terrain;
	private int x = 0;
	private int y = 0;
	private final static Logger logger = LoggerFactory.getLogger(MapNode.class);

	/**
	 * Helper class, to hold information about a Field on the HalfMap
	 * 
	 * @param x x Coordinate of the node
	 * @param y y Coordinate of the node
	 */
	public MapNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Uses basic Math.random() to get a random terrain type and return this.
	 * 
	 * @return A randomly selected terrain type
	 */
	public ETerrain generateRandomTerrain() {

		ETerrain[] terrainTypes = ETerrain.values();

		int randomIndex = (int) (Math.random() * terrainTypes.length);
		logger.info("Return the following terrain to the caller:{}", terrainTypes[randomIndex]);

		return terrainTypes[randomIndex];
	}

	public void setCastlePos() {
		if (terrain == ETerrain.Grass) {
			this.hasCastle = true;
		}
	}

	public ETerrain getTerrain() {
		return terrain;
	}

	public void setTerrain(ETerrain terrain) {
		this.terrain = terrain;
	}

	public boolean hasCastle() {
		return this.hasCastle;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}
