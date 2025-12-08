package client.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.ETerrain;

public class MapHalfGenerator {

	private static MapNode[][] mapNode;
	private static final int WIDTH = 10;
	private static final int HEIGHT = 5;
	private final static Logger logger = LoggerFactory.getLogger(MapHalfGenerator.class);
	private final static double GRASS_FIELDS = 0.48;
	private final static double MOUNTAIN_FIELDS = 0.1;
	private final static double WATER_FIELDS = 0.14;
	private final static double CASTLE_FIELDS = 0.02;
	private int totalCastles = (int) Math.round((WIDTH * HEIGHT) * CASTLE_FIELDS);
	private List<Point> grassTiles = new ArrayList<>(totalCastles);

	/**
	 * Generates the half map, which is sent to the server. Implicitly calls generateMap.
	 */
	public MapHalfGenerator() {

		mapNode = new MapNode[WIDTH][HEIGHT];

		logger.info("The CTOR for a MapHalf has been called!");

		logger.info("Generating map...");
		generateMap();

	};

	/**
	 * Main generation algorithm, calls the floodFill method to actually fill the
	 * tiles with random terrain according to the rules from the game idea
	 */
	private void generateMap() {
		mapNode = new MapNode[WIDTH][HEIGHT];
		boolean[][] visited = new boolean[WIDTH][HEIGHT];

		int totalTiles = WIDTH * HEIGHT;
		int mountainTiles = (int) (totalTiles * MOUNTAIN_FIELDS);
		int grassTiles = (int) (totalTiles * GRASS_FIELDS);
		int waterTiles = (int) (totalTiles * WATER_FIELDS);
		int otherTiles = totalTiles - mountainTiles - grassTiles - waterTiles;

		fillTerrain(ETerrain.Water, waterTiles, visited);
		fillTerrain(ETerrain.Mountain, mountainTiles, visited);
		fillTerrain(ETerrain.Grass, grassTiles, visited);
		ETerrain[] remaining = { ETerrain.Grass, ETerrain.Mountain };
		fillRemaining(remaining, otherTiles, visited); // fill others randomly with grass or mountain and not water,
														// since water field threshold is reached already
		placeCastle();

	}

	/**
	 * Basic flood fill algorithm implementation in pseudo code from Wikipedia
	 * 
	 * @param terrain The terrain with which the nodes are to be filled
	 * @param count How many fields are to be filled with the given terrain type
	 * @param visited Assigns a boolean value to each field, if it has been visited yet or not, so to not fill a field twice
	 */
	private void fillTerrain(ETerrain terrain, int count, boolean[][] visited) {
		Random rand = new Random();
		int filled = 0;
		int waterEdgeCount = 0;

		while (filled < count) {
			int startX = rand.nextInt(WIDTH);
			int startY = rand.nextInt(HEIGHT);

			if (visited[startX][startY])
				continue;

			Queue<int[]> queue = new LinkedList<>();
			queue.add(new int[] { startX, startY });

			while (!queue.isEmpty() && filled < count) {
				int[] pos = queue.poll();
				int x = pos[0];
				int y = pos[1];

				if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT || visited[x][y])
					continue;

				if (terrain == ETerrain.Water && isEdge(x, y) && waterEdgeCount >= 2)
					continue;

				visited[x][y] = true;
				if (mapNode[x][y] == null) {
					mapNode[x][y] = new MapNode(x, y);
				}
				mapNode[x][y].setTerrain(terrain);
				filled++;

				if (terrain == ETerrain.Water && isEdge(x, y)) {
					waterEdgeCount++;
				}

				if (mapNode[x][y].getTerrain() == ETerrain.Grass) {
					grassTiles.add(new Point(x, y));
				}

				queue.add(new int[] { x + 1, y });
				queue.add(new int[] { x - 1, y });
				queue.add(new int[] { x, y + 1 });
				queue.add(new int[] { x, y - 1 });
			}
		}

		logger.info("Flood filled {} tiles of {}", count, terrain);
	}

	/**
	 * 
	 * Fills the remaining tiles with a terrainType that has not been used up to it's capacity
	 * 
	 * @param terrainTypes Terrain types which can be used
	 * @param count How many fields remain to be filled
	 * @param visited 2-D array to check if a field has been visited already or not
	 */
	private void fillRemaining(ETerrain[] terrainTypes, int count, boolean[][] visited) {
		Random rand = new Random();
		int filled = 0;

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (!visited[x][y]) {
					ETerrain randomTerrain = terrainTypes[rand.nextInt(terrainTypes.length)];
					mapNode[x][y] = new MapNode(x, y);
					mapNode[x][y].setTerrain(randomTerrain);
					visited[x][y] = true;
					filled++;
				}
			}
		}

		logger.info("Filled {} remaining tiles with random terrain", filled);
	}

	/**
	 * 
	 * Places castle(s) up to the amount required by the server on random tiles 
	 * 
	 */
	private void placeCastle() {
		logger.info("Placing the necessary castles");

		Random rng = new Random();

		Collections.shuffle(grassTiles, rng);

		for (int i = 0; i < totalCastles; i++) {
			Point nextPoint = grassTiles.get(i);
			mapNode[nextPoint.x][nextPoint.y].setCastlePos();
			logger.info("Placed castle at position x: {} and y: {}", nextPoint.x, nextPoint.y);
		}
	}

	public MapNode[][] getMap() {
		return mapNode;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	/**
	 * 
	 * Checks if a given coordinate is on an edge of the half map, by taking it's minimum and maximum coordinate 
	 * 
	 * @param x x-Coordinate of the field to be checked 
	 * @param y y-Coordinate of the field to be checked
	 * @return true if it is on an edge, false if not
	 */
	private boolean isEdge(int x, int y) {
		return x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1;
	}
}
