package client.movement;

import java.awt.Point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;

public class MapLayout {
	
    private static final Logger logger = LoggerFactory.getLogger(MapLayout.class);

	private boolean splitVertically;
	private boolean fortInFirstHalf;

	/**
	 * 
	 * Helper class to check if the map is split vertically and if the fort is spawned in the left/right or top/bottom half
	 * With this we can determine where the client spawned and thus make a boundary for treasure searching and castle searching.
	 * 
	 * @param map The local client side map
	 * @param playerPos The players spawn position
	 */
	public MapLayout(TerrainMap map, Point playerPos) {
        this.splitVertically = map.getXDimension() > map.getYDimension();
        this.fortInFirstHalf = splitVertically 
            ? playerPos.x < map.getXDimension() / 2
            : playerPos.y < map.getYDimension() / 2;
        logger.info("Map split vertically: {}, Fort in first half: {}", 
                splitVertically, fortInFirstHalf);
	}
	
	public boolean getSplitVertically() {
		return splitVertically;
	}
	public boolean getFortInFirstHalf() {
		return fortInFirstHalf;
	}
	
}
