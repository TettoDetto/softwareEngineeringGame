package client.map.placers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import messagesbase.messagesfromclient.ETerrain;

public class CheckTerrainTypeAmount {
	
	private int WIDTH;
	private int HEIGHT;
	private MapNode[][] mapHalf;

	public CheckTerrainTypeAmount(int WIDTH, int HEIGHT, MapNode[][] mapHalf) {
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
		List<MapNode> candidates = new ArrayList<>();
		int terrainCount = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				MapNode node = mapHalf[x][y];
				
				if (node.getTerrain() == terrain) {
					terrainCount++;
					continue;
				}
				
				if (node.hasCastle()) {
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
			node.setTerrain(terrain);
			missing--;
		}
		
	}
}
