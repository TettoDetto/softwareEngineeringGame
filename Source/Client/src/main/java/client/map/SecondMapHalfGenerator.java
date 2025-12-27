package client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.CastlePlacer;
import client.map.placers.CheckBorders;
import client.map.placers.EBorder;
import client.map.placers.MapNode;
import client.map.placers.PlaceBorderFields;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMap;

public class SecondMapHalfGenerator extends MapHalfGenerator {

	private final static Logger logger = LoggerFactory.getLogger(SecondMapHalfGenerator.class);
	private Set<EBorder> borders = new HashSet<>();
	private final static int grassFieldsAmount = 24;
	private final static int mountainFieldsAmount = 5;
	private final static int waterFieldsAmount = 7; 
	
	public SecondMapHalfGenerator(FullMap fullMap) {
		super(fullMap);
	}

	/**
	 *	Used when the client needs to create the second half map. Calls {@link MapHalfGenerator#generateMap()} for the basic floodFill algorithm. 
	 *	This fills the map with the correct amount of terrain types. Afterwards this method checks the borders of the half map to adhere to the rules.
	 *	Calls {@link CheckBorders} for the placement of correct border {@link ETerrain} fields.
	 *	To make sure the map is correct, this method checks for any missing {@link ETerrain} fields, if there are not enough of them on the {@link MapNode[][]} HalfMap.
	 *
	 */
	@Override
	public void generateMap() {
		
		super.generateMap();
		
		CheckBorders checkBorders = new CheckBorders(fullMap);
		
		this.borders = checkBorders.checkConnectingBorders();
		
		checkBorders.matchBorder(mapHalf, borders);
		
		PlaceBorderFields placeBorderFields = new PlaceBorderFields(mapHalf);
		
		for (EBorder border : borders) {
			placeBorderFields.setConnectingBorder(border);
		}
		
		placeBorderFields.checkBorderRules(false);
		mapHalf = placeBorderFields.getNewMap();
		
		ensureCorrectAmountOfFields(ETerrain.Water, Math.max(waterFieldsAmount, (int) Math.ceil((WIDTH*HEIGHT)*WATER_FIELDS)));
		ensureCorrectAmountOfFields(ETerrain.Mountain, Math.max(mountainFieldsAmount, (int) Math.ceil((WIDTH*HEIGHT)*MOUNTAIN_FIELDS)));
		ensureCorrectAmountOfFields(ETerrain.Grass, Math.max(grassFieldsAmount, (int) Math.ceil((WIDTH*HEIGHT)*GRASS_FIELDS)));
		new CastlePlacer();
		CastlePlacer.placeCastle(WIDTH, HEIGHT, borders, mapHalf);
		
	}
	
	/**
	 * For a specific {@link ETerrain} type, this method checks the required amount
	 * 
	 * @param terrain
	 * @param required
	 */
	private void ensureCorrectAmountOfFields(ETerrain terrain, int required) {
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
};
