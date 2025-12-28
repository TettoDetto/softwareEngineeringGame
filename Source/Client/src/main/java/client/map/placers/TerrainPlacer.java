package client.map.placers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.MapHalfGenerator;
import messagesbase.messagesfromclient.ETerrain;

public class TerrainPlacer {

	private final static Logger logger = LoggerFactory.getLogger(TerrainPlacer.class);
	
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private MapNode[][] halfMap;
	private boolean[][] visited;
	
	/**
	 * 
	 * Places {@link ETerrain#Mountain} across the HalfMap in a specific way, in order to abuse the visibility gain, which they give.
	 * 
	 * @param amount The amount of mountains the method should place
	 * @param visited The fields of the HalfMap which have already been visited
	 */
	public void placeFields(int amount, ETerrain type, boolean[][] visited, MapNode[][] halfMap) {
		this.visited = visited;
		this.halfMap = halfMap;
		int placed = 0;
		
		List<int[]> candidates = new ArrayList<>();
		
		logger.info("Placing Terrain Type: " + type);
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				
				if(!visited[x][y]) {
					candidates.add(new int[] {x,y});
				}
			}
		}
		
		Collections.shuffle(candidates);
		logger.info("Shuffling candidates");
		
		while (placed < amount && !candidates.isEmpty()) {
				
			int x = candidates.getLast()[0];
			int y = candidates.getLast()[1];
			candidates.remove(candidates.size()-1);
			
			
			if (visited[x][y]) { 
				continue;
			}
			
			if (MapHalfGenerator.isOutsideOfMap(x,y)) {
				continue;
			}
			
			logger.info("Placing a {} at node {},{}", type, x,y);
			halfMap[x][y] = new MapNode(x,y);
			halfMap[x][y].setTerrain(type);
			visited[x][y] = true;
			placed++;
				
		}
		
	}
	
	public MapNode[][] getMap() {
		return halfMap;
	}
	public boolean[][] getVisited() {
		return visited;
	}
	
	public void setHeight(int height) {
		this.HEIGHT = height;
	}
	
	public void setWidth(int width) {
		this.WIDTH = width;
	}
}
