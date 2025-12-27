package client.map.placers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.ETerrain;

public class PlaceBorderFields {

	private MapNode[][] mapNode;
	private List<EBorder> connectingBorder = new ArrayList<>();
	private static final int WIDTH = 10;
	private static final int HEIGHT = 5;
	private static final double ENTERABLE_FIELDS_PERCENTAGE = 0.4;
	private static final double NON_ENTERABLE_FIELDS_PERCENTAGE = 0.2;
	private final static Logger logger = LoggerFactory.getLogger(PlaceBorderFields.class);
	
	public PlaceBorderFields(MapNode[][] mapNode) {
		this.mapNode = mapNode;
	}
	
	public void checkBorderRules(boolean excludeBorder) {
	
		if (excludeBorder) {
			if (!connectingBorder.contains(EBorder.Left)) {
				checkBorder(leftBorder());
			}
			if (!connectingBorder.contains(EBorder.Right)) {
				checkBorder(rightBorder());
			}
			if (!connectingBorder.contains(EBorder.Top)) {
				checkBorder(topBorder());
			}
			if (!connectingBorder.contains(EBorder.Bottom)) {
				checkBorder(bottomBorder());
			}
		}
		else {
			
			checkBorder(leftBorder());
			checkBorder(rightBorder());
			checkBorder(topBorder());
			checkBorder(bottomBorder());
		}
		
	}
	
	private List<int []> leftBorder() {
		List<int []> result = new ArrayList<>();
		for (int y = 0; y < HEIGHT; y++) {
			result.add(new int[]{0, y});
		}
		logger.info("Left Border has a size of {}", result.size());
		return result;
	}
	private List<int []> rightBorder() {
		List<int []> result = new ArrayList<>();
		for (int y = 0; y < HEIGHT; y++) {
			result.add(new int[]{WIDTH-1, y});
		}
		logger.info("Right Border has a size of {}", result.size());
		return result;
	}
	private List<int[]> topBorder() {
		List<int []> result = new ArrayList<>();
		for (int x = 0; x < WIDTH; x++) {
			result.add(new int[]{x, HEIGHT-1});
		}
		logger.info("Top Border has a size of {}", result.size());
		return result;
	}
	private List<int[]> bottomBorder() {
		List<int []> result = new ArrayList<>();
		for (int x = 0; x < WIDTH; x++) {
			result.add(new int[]{x, 0});
		}
		logger.info("Bottom Border has a size of {}", result.size());
		return result;
	}
	
	private void checkBorder(List<int[]> border) {
		int fieldsOnBorder = border.size();
		
		int MIN_ENTERABLE_FIELDS = (int) Math.ceil(fieldsOnBorder*ENTERABLE_FIELDS_PERCENTAGE);
		int MIN_NON_ENTERABLE_FIELDS = (int) Math.ceil(fieldsOnBorder*NON_ENTERABLE_FIELDS_PERCENTAGE);
	
		
		List<int[]> enterableFields = new ArrayList<>();
		List<int[]> nonEnterableFields = new ArrayList<>();
		
		for (int[] point : border) {
			
			int x = point[0];
			int y = point[1];
			
			if (mapNode[x][y].getTerrain() == ETerrain.Water) {
				nonEnterableFields.add(point);
			}
			
			else {
				enterableFields.add(point);
			}	
		}
		
		logger.info("Border has {} non-enterable fields and {} enterable fields", nonEnterableFields.size(), enterableFields.size());
		logger.info("There need to be at least {} non-enterable fields and {} enterable fields", MIN_NON_ENTERABLE_FIELDS, MIN_ENTERABLE_FIELDS);
	
		while(nonEnterableFields.size() < MIN_NON_ENTERABLE_FIELDS && !enterableFields.isEmpty()) {
			
			logger.info("Placing non enterable field");
			
			Random rand = new Random();
			
			int index = rand.nextInt(enterableFields.size());
			int[] node = enterableFields.get(index);
			
			int x = node[0];
			int y = node[1];
 			
			if (mapNode[x][y].hasCastle()) {
				logger.info("There is the castle on position [{}, {}]", x, y);
				enterableFields.remove(node);
				continue;
			}
			
			if (isCorner(x,y)) {
				logger.info("Skipped corner [{}, {}]", x,y);
				continue;
			}
			
			mapNode[x][y].setTerrain(ETerrain.Water);
			logger.info("Set the terrain at node [{}, {}] to water", x, y); 
			
			nonEnterableFields.add(node);
			enterableFields.remove(node);
		}
		
		while(enterableFields.size() < MIN_ENTERABLE_FIELDS && !nonEnterableFields.isEmpty()) {
			
			logger.info("Placing enterable field");
			
			Random rand = new Random();
			
			int index = rand.nextInt(nonEnterableFields.size());
			int[] node = nonEnterableFields.get(index);
			
			int x = node[0];
			int y = node[1];
			
			
			if (isCorner(x,y)) {
				logger.info("Skipped corner [{}, {}]", x,y);
				continue;
			}
			
			mapNode[x][y].setTerrain(ETerrain.Grass);
			logger.info("Set the terrain at node [{}, {}] to grass", x, y); 
			
			enterableFields.add(node);
			nonEnterableFields.remove(node);
		}
	}
	
	private boolean isCorner(int x, int y) {
	    return (x == 0 || x == WIDTH - 1) &&
	            (y == 0 || y == HEIGHT - 1);
	}

	public MapNode[][] getNewMap() {
		return mapNode;
	}

	public void setConnectingBorder(EBorder border) {
		this.connectingBorder.add(border);		
	}
	
}
