package client.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.placers.PlaceBorderFields;

public class FirstMapHalfGenerator extends MapHalfGenerator {

	protected final static Logger logger = LoggerFactory.getLogger(FirstMapHalfGenerator.class);
	
	/**
	 * Generates the half map, which is sent to the server. Implicitly calls generateMap.
	 */
	
	public FirstMapHalfGenerator() {	
		super();

	};

	@Override
	public void generateMap() {
		
		super.generateMap();
		
		PlaceBorderFields placeBorderFields = new PlaceBorderFields(mapHalf);
		placeBorderFields.checkBorderRules(false);
		mapHalf = placeBorderFields.getNewMap();
		super.placeCastle();

	}

}
