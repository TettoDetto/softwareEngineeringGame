package client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.MapNode;
import messagesbase.messagesfromclient.ETerrain;

public class CorrectFieldsAmount {
	private final static Logger logger = LoggerFactory.getLogger(CorrectFieldsAmount.class);
	private int WIDTH;
	private int HEIGHT;
	private MapNode[][] mapHalf;

	public CorrectFieldsAmount (int WIDTH, int HEIGHT, MapNode[][] mapHalf) {
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.mapHalf = mapHalf;
	}
	
	/**
	 * For a specific {@link ETerrain} type, this method checks the required amount
	 * 
	 * @param terrain
	 * @param required
	 */
	public void ensureCorrectAmountOfFields(ETerrain terrain, int required) {
		logger.info("Checking if we have enough fields of terrain: {}", terrain.toString());
		List<MapNode> candidates = new ArrayList<>();
		int terrainCount = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				MapNode node = mapHalf[x][y];
				
				if (node.getTerrain() == terrain) {
					terrainCount++;
					continue;
				}
				
				if (node.hasCastle() || isEdge(x,y)) {
					continue;
				}
				
				candidates.add(node);					
			}
		}
		
		int missing = required - terrainCount;
		if (missing <= 0) {
			return;
		}
		
		Collections.shuffle(candidates);
		
		for (MapNode node : candidates) {
			if (missing <= 0) {
				break;
			}
			logger.info("We don't have enough fields of terrain type: {} changing field from: {}", terrain, node.getTerrain());
			node.setTerrain(terrain);
			missing--;
		}
		
	}
	
	public boolean isEdge(int x, int y) {
		return x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1;
	}
	
}
