package client.map;

import client.map.placers.PlaceBorderFields;

public class FirstMapHalfGenerator extends MapHalfGenerator {

	
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
		placeBorderFields.checkBorderRules();
		mapHalf = placeBorderFields.getNewMap();
		super.checkFieldsAmount();
		super.placeCastle();
	}

}
