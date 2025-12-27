package client.map.validation;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.MapHalfService;
import client.map.NeighborsOfMapNode;
import client.map.placers.MapNode;
import client.movement.model.ValidateCoordinate;
import messagesbase.messagesfromclient.ETerrain;

public class MapValidator {
	final Logger logger = LoggerFactory.getLogger(MapHalfService.class);
	private MapNode[][] map;
	private int WIDTH = 0;
	private int HEIGHT = 0;
	private int totalLand = 0;
	private int reachableLand = 0;
	private int startX = -1;
	private int startY = -1;
	private NeighborsOfMapNode neighbors;
	private int castleCount = 0;
	/**
	 * 
	 * Validates the correctness of a map, by checking it's terrain types and castle placements
	 * 
	 * @param map The half map to be validated
	 */
	public MapValidator(MapNode[][] map) {
		this.map = map;
		this.WIDTH = map.length;
		this.HEIGHT = map[0].length;
		this.neighbors = new NeighborsOfMapNode(map);
	}

	/**
	 * 
	 * Iterates over the map and calls helper functions to validate the correctness of the half map.
	 * 
	 * @return True if the map fulfills expectations, false if something went wrong with creating the map
	 */
	public ValidationResult isValidMap() {

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (map[x][y].getTerrain() == ETerrain.Grass && map[x][y].hasCastle()) {
					startX = x;
					startY = y;
					break;
				}
			}
		}
		
		checkReachableLand();
		checkCastleAmount();
		
		if (startX < 0 || startY < 0) {
			logger.error("There was a mistake while placing the castle during map creation");
			return new ValidationResult(false, EValidationResults.INVALID_START_COORDINATES);
		}

		if (totalLand == reachableLand && castleCount == 1) {
			logger.info("The map has been generated correctly");
			return new ValidationResult(true, EValidationResults.MAP_CORRECT);
		} 
		else if (totalLand != reachableLand) {
			logger.error("While validating the map, an unreachable field has been detected. Only " + reachableLand
					+ " out of " + totalLand + " Fields are reachable");
			return new ValidationResult(false, EValidationResults.UNREACHABLE_LAND);
		}
		else {
			logger.error("An unexpected error happended during map half creation");
			return new ValidationResult(false, EValidationResults.UNEXPECTED_ERROR);
		}
	}

	/**
	 * Helper method to check if there are any non-accessible fields
	 */
	private void checkReachableLand() {
		
		boolean[][] visited = new boolean[WIDTH][HEIGHT];
		Queue<int[]> queueOfNodes = new LinkedList<>();
		if (startX < 0 || startY < 0) {
			return;
		}
		visited[startX][startY] = true;
		queueOfNodes.add(new int[] { startX, startY });

		while (!queueOfNodes.isEmpty()) {

			int[] node = queueOfNodes.poll();
			int x = node[0];
			int y = node[1];
			

			reachableLand++;

			List<Point> neighborsOfNode = neighbors.getNeighbors(new Point(x, y));
			for (Point p : neighborsOfNode) {
				if (!visited[p.x][p.y]) {
					visited[p.x][p.y] = true;
					queueOfNodes.add(new int[] { p.x, p.y });
				}
			}
		}
	}
	
	/**
	 * Helper method the check if there is the correct amount of castle(s)
	 */
	private void checkCastleAmount() {

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				new ValidateCoordinate();
				if (map[x][y].getTerrain() != ETerrain.Water
						&& ValidateCoordinate.checkCoordinateOfMapHalf(x, y, map)) {
					totalLand++;
				}
				if (map[x][y].hasCastle()) {
					castleCount++;
				}
			}
		}
	}
}
