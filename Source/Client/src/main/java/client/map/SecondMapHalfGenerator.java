package client.map;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.CastlePlacer;
import client.map.placers.CheckBorders;
import client.map.placers.EBorder;
import client.map.placers.PlaceBorderFields;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMap;

public class SecondMapHalfGenerator extends MapHalfGenerator {

	private final static Logger logger = LoggerFactory.getLogger(SecondMapHalfGenerator.class);
	private Set<EBorder> borders = new HashSet<>();

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
		
		logger.info("Generating the second half map");
		
		CheckBorders checkBorders = new CheckBorders(fullMap);
		
		this.borders = checkBorders.checkConnectingBorders();
		
		checkBorders.matchBorder(mapHalf, borders);
		
		PlaceBorderFields placeBorderFields = new PlaceBorderFields(mapHalf);
		
		for (EBorder border : borders) {
			placeBorderFields.setConnectingBorder(border);
		}
		
		placeBorderFields.checkBorderRules();
		mapHalf = placeBorderFields.getNewMap();		

		super.checkFieldsAmount();
		new CastlePlacer();
		CastlePlacer.placeCastle(WIDTH, HEIGHT, borders, mapHalf);
	}
};
