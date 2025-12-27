package client.map.placers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.MapHalfGenerator;
import messagesbase.messagesfromclient.ETerrain;

public class MountainPlacer {

	private final static Logger logger = LoggerFactory.getLogger(MountainPlacer.class);
	
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
	public void placeMountains(int amount, boolean[][] visited, MapNode[][] halfMap) {
		this.visited = visited;
		this.halfMap = halfMap;
		int placed = 0;
		int minDistance = 3;
		
		List<int[]> candidates = new ArrayList<>();
		
		logger.info("Placing mountains");
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				
				if(!visited[x][y]) {
					logger.info("Adding a candidate");
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
			
			logger.info("Looking at node {},{}", x,y);
			
			if (visited[x][y]) { 
				continue;
			}
			
			if (MapHalfGenerator.isOutsideOfMap(x,y)) {
				continue;
			}
			
			
			if (!hasMountainAsNeighbor(x,y, minDistance)) {
				logger.info("Placing a mountain at node {},{}", x,y);
				halfMap[x][y] = new MapNode(x,y);
				halfMap[x][y].setTerrain(ETerrain.Mountain);
				visited[x][y] = true;
				placed++;
				
			}
		}
		
	}

	/**
	 * 
	 * Helper method of {@link #placeMountains(int, boolean[][])}.
	 * Looks at a {@link ETerrain#Mountain} specific by parameters {@code int x} and {@code int y}
	 * and determines if it has {@link ETerrain#Mountain} as neighbors specified by the {@code int minDistance}
	 * 
	 * @param x x-Coordinate of a {@link ETerrain#Mountain}
	 * @param y y-Coordinate of a {@link ETerrain#Mountain}
	 * @param minDistance The minimum distance to still considered a neighbor
	 * @return {@code true} if a mountain has another mountain field as neighbor, 
	 * {@code false} if not
	 */
	private boolean hasMountainAsNeighbor(int x, int y, int minDistance) {
		for (int dx = -minDistance; dx <= minDistance; dx++) {
			for (int dy = -minDistance; dy <= minDistance; dy++) {
				int nextX = x+dx;
				int nextY = y+dy;
				
				if (MapHalfGenerator.isOutsideOfMap(nextX, nextY)) {
					continue;
				}
				if (halfMap[nextX][nextY]!=null && halfMap[nextX][nextY].getTerrain() == ETerrain.Mountain) {
					return true;
				}
			}
		}
		return false;
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
