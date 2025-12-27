package client.map.placers;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

public class CheckBorders {

	private final static Logger logger = LoggerFactory.getLogger(CheckBorders.class);
	
	private FullMap fullMap;
	
	public CheckBorders(FullMap fullMap) {
		this.fullMap = fullMap;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Set<EBorder> checkConnectingBorders() {
		
		Set<EBorder> connectingBorders = new HashSet<>();
		
		int maxX = 0;
		int maxY = 0;
		int minX = 100;
		int minY = 100;
        
        for (FullMapNode node : fullMap.getMapNodes()) {
        	int x = node.getX();
        	int y = node.getY();
        	
        	if (x < minX) {
        		minX = x;
        	}
        	
        	if (x > maxX) {
        		maxX = x;
        	}
        	
        	if (y < minY) {
        		minY = y;
        	}
        	
        	if (y > maxY) {
        		maxY = y;
        	}
        }
		logger.info("Existing first map in FullMap is from [{}, {}] to [{}, {}]", minX, minY, maxX, maxY);
		
		//If the map is 20x5, that means the first map half is on the left side of the full map and it can either have a horizontal or a vertical layout
		//That means our left border (x=0) is the first map halves right border (x=4). If their minimum x is not 0, it means that their left border is on the right side of the full map
		
		if (maxY == 4) {
			if (minX == 0) {
				logger.info("The first map half is on the left");				
				connectingBorders.add(EBorder.Left);		
			}
			else {
				logger.info("The first map half is on the right");
				connectingBorders.add(EBorder.Right);
			}
		}
		
		//If the map is 10x10, it means the first map is on the top half of the full map and it has a vertical layout
		//That means our top border (y = 9) is their bottom border (y = 0) and thus the full map has a dimension of y = 20
		
		if (maxX == 9) {
			if (minY == 0) {
				logger.info("The first map half is on the bottom");
				connectingBorders.add(EBorder.Bottom);
			}
			else {				
				logger.info("The first map half is on the top");
				connectingBorders.add(EBorder.Top);
			}
		}	
		return connectingBorders;
	}
	
	
	/**
	 * Calls the {@link BorderStrategy} class for the {@code Set<EBorder> borders} connecting borders. Returns the newly updated @{code MapNode[][]}.
	 * 
	 * @param mapNode Current HalfMap to be checked against the connecting borders
	 * @param borders {@code Set<EBorder>} of connecting borders
	 * @return A newly updated {@code MapNode[][]} halfMap, with all possible connecting borders respected
	 */
	public MapNode[][] matchBorder(MapNode[][] halfMap, Set<EBorder> borders) {
		logger.info("We need to respect: {} borders", borders.size() );
		halfMap = new BorderStrategy(borders, fullMap, halfMap).getNewMap();
		return halfMap;
	}
	
}
