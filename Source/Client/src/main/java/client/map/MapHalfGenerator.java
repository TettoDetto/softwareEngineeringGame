package client.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.MapNode;
import client.map.placers.TerrainPlacer;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMap;

public abstract class MapHalfGenerator {

	protected MapNode[][] mapHalf;
	protected FullMap fullMap;
	protected static final int WIDTH = 10;
	protected static final int HEIGHT = 5;
	protected final static double GRASS_FIELDS = 0.48;
	protected final static double MOUNTAIN_FIELDS = 0.10;
	protected final static double WATER_FIELDS = 0.14;
	protected final static double REAL_WATER_FIELDS = 0.04;
	protected final static double CASTLE_FIELDS = 0.02;
	protected static final int NON_REACHABLE_FIELDS = 6;
	protected List<Point> grassTiles = new ArrayList<>(1);


	
	protected final static Logger logger = LoggerFactory.getLogger(MapHalfGenerator.class);

	/**
	 * Generates the half map, which is sent to the server. Implicitly calls generateMap.
	 */
	
	public MapHalfGenerator(FullMap fullMap) {

		mapHalf = new MapNode[WIDTH][HEIGHT];
		this.fullMap = fullMap;


		logger.info("The CTOR for a MapHalf has been called!");

		logger.info("Generating map...");
		generateMap();

	};
	
	public MapHalfGenerator() {

		mapHalf = new MapNode[WIDTH][HEIGHT];
		logger.info("The CTOR for a MapHalf has been called!");

		logger.info("Generating map...");
		generateMap();

	};
	

	/**
	 * 
	 * Generates a generic map, that adheres to the rules of terrain types and placement of terrain. Useful, when you only need to generate the first HalMap.
	 * 
	 * Any subclass implementing this method needs to either call {@link #placeCastle} from the super class, or override it themselves.
	 */
	protected void generateMap() {
		logger.info("Generating the second map half, need to take care of the border fields");
		
		mapHalf = new MapNode[WIDTH][HEIGHT];
		boolean[][] visited = new boolean[WIDTH][HEIGHT];

		int totalTiles = WIDTH * HEIGHT;
		int mountainTiles = (int) (totalTiles * MOUNTAIN_FIELDS);
		int grassTiles = (int) (totalTiles * GRASS_FIELDS);
		int waterTiles = (int) (totalTiles * REAL_WATER_FIELDS);
		int otherTiles = totalTiles - mountainTiles - grassTiles - waterTiles;

		TerrainPlacer terrainPlacer = new TerrainPlacer();
		terrainPlacer.setHeight(HEIGHT);
		terrainPlacer.setWidth(WIDTH);
		terrainPlacer.placeFields(mountainTiles, ETerrain.Mountain, visited, mapHalf);
		
		mapHalf = terrainPlacer.getMap();
		visited = terrainPlacer.getVisited();
		
		terrainPlacer.placeFields(waterTiles, ETerrain.Water, visited, mapHalf);
		mapHalf = terrainPlacer.getMap();
		visited = terrainPlacer.getVisited();
		
		terrainPlacer.placeFields(grassTiles, ETerrain.Grass, visited, mapHalf);
		mapHalf = terrainPlacer.getMap();
		visited = terrainPlacer.getVisited();
		
		terrainPlacer.placeFields(otherTiles, ETerrain.Mountain, visited, mapHalf);
	}

	/**
	 * 
	 * Places castle(s) up to the amount required by the server on random tiles 
	 * 
	 */
	protected void placeCastle() {
		logger.info("Placing the necessary castles");
		
		List<MapNode> validCastleSpots = new ArrayList<>();
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				MapNode node = mapHalf[x][y];
				
				if (node == null) continue;

				if (node.getTerrain() == ETerrain.Grass && !isEdge(x, y)) {
					validCastleSpots.add(node);
				}
			}
		}

		if (!validCastleSpots.isEmpty()) {
			Collections.shuffle(validCastleSpots);
			
			MapNode chosenNode = validCastleSpots.get(0);
			
			chosenNode.setCastlePos();
			
			logger.info("Placed castle at position x: {} and y: {}", chosenNode.getX(), chosenNode.getY());
		} else {
			logger.error("Could not place castle, no valid grass field found inside borders.");
		}
	}
	
	protected void checkFieldsAmount() {
		CorrectFieldsAmount fieldsAmount = new CorrectFieldsAmount(WIDTH, HEIGHT, mapHalf);
		fieldsAmount.ensureCorrectAmountOfFields(ETerrain.Water, (int) Math.ceil((WIDTH*HEIGHT)*WATER_FIELDS));
		fieldsAmount.ensureCorrectAmountOfFields(ETerrain.Mountain, (int) Math.ceil((WIDTH*HEIGHT)*MOUNTAIN_FIELDS));
		fieldsAmount.ensureCorrectAmountOfFields(ETerrain.Grass, (int) Math.ceil((WIDTH*HEIGHT)*GRASS_FIELDS));
	}
	
	/**
	 * 
	 * Checks if a given coordinate is on an edge of the half map, by taking it's minimum and maximum coordinate 
	 * 
	 * @param x x-Coordinate of the field to be checked 
	 * @param y y-Coordinate of the field to be checked
	 * @return true if it is on an edge, false if not
	 */
	public boolean isEdge(int x, int y) {
		return x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1;
	}
	
	public static boolean isOutsideOfMap(int x, int y) {
		return (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT);
	}


	public MapNode[][] getMap() {
		return mapHalf;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}
}
