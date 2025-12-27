package client.map.placers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.ETerrain;

public class CastlePlacer {
	
	private final static Logger logger = LoggerFactory.getLogger(CastlePlacer.class);
	
	/**
	 * Places a castle as far away from the connecting border as possible. It determines this by calculating the Euclidean distance for a given border.
	 */
	public static void placeCastle(int WIDTH, int HEIGHT, Set<EBorder> borders, MapNode[][] mapHalf) {
		
		int targetX = 0; 
		int targetY = 0;
		
		if (borders.contains(EBorder.Left) || borders.contains(EBorder.Bottom)) {
			targetX = WIDTH - 1;
			targetY = HEIGHT - 1;
		}
		
		else if (borders.contains(EBorder.Right)) {
			targetX = 0;
			targetY = HEIGHT - 1;
		}
		else if (borders.contains(EBorder.Top)) {
			targetX = WIDTH / 2;
			targetY = 0;
		}
		
		MapNode candidate = null;
		double minDistance = Double.POSITIVE_INFINITY;
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				
				
				MapNode current = mapHalf[x][y];
				if (current.getTerrain() == ETerrain.Grass) {
					
					double distance = Math.sqrt(Math.pow(x-targetX, 2) + Math.pow(y-targetY, 2));
					if (distance < minDistance) {
						minDistance = distance;
						candidate = current;
					}
				}
				
				
			}
		}
		if (candidate != null) {
			candidate.setCastlePos();
		}
		else {
			logger.error("Couldn't find castle position");
		}
	}
}
