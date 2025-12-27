package client.movement;

import java.awt.Point;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.movement.model.ValidateCoordinate;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;

public class VisibilityGain {
	
    private static final Logger logger = LoggerFactory.getLogger(VisibilityGain.class);

	private TerrainMap map;
	private Set<Point> discovered;
	private ProbabilityMap probMap;
	
	/**
	 * 
	 * Helper class to calculate the value of a mountain field by calculating 
	 * how much grass fields it uncovers. 
	 * 
	 * @param map Local representation of the server side FullMap
	 * @param discovered Set of fields uncovered by mountains already
	 * @param currentPath The Queue that holds the current path
	 * @param probMap The probability map that holds the likelihood of the treasure or castle being on any specific field 
	 */
	public VisibilityGain(TerrainMap map, Set<Point> discovered,  ProbabilityMap probMap) {
		this.map = map;
		this.discovered = discovered;
		this.probMap = probMap;
	}
	
    /**
     * 
     * Calculates the score of a specific mountain field, based on how many grass fields it uncovers
     * 
     * @param pos The mountain field to be calculated on
     * @return The score or 0 if no fields uncovered
     */
    public int calculateVisibilityGain(Point pos) {
        FullMapNode node = map.getMapNode(pos.x, pos.y);
        
        if (node == null) {
            return 0;
        }
        
        int gain = 0;
        
        if (node.getTerrain() == ETerrain.Mountain) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    
                    int nx = pos.x + dx;
                    int ny = pos.y + dy;
                    
                    if (!ValidateCoordinate.checkCoordinate(nx, ny, map)) continue;
                    
                    Point neighbor = new Point(nx, ny);
                    FullMapNode neighborNode = map.getMapNode(nx, ny);
                    
                    if (!discovered.contains(neighbor) 
                        && neighborNode != null 
                        && neighborNode.getTerrain() == ETerrain.Grass) {
                        gain++;
                    }
                }
            }
        } else if (!discovered.contains(pos)) {
            gain = 1;
        }
        
        return gain;
    }
	
	/**
	 * 
	 * Iterates over the discovered nodes to detect any objectives (Treasure or castle) already to discovered 
	 * 
	 * @param hasTreasure
	 * @return A point if it has an objective 
	 */
	public Point scanForVisibleObjective(boolean hasTreasure) {
        for (Point p : discovered) {
            if (!ValidateCoordinate.checkCoordinate(p.x, p.y, map)) {
                continue;
            }
            
            FullMapNode node = map.getMapNode(p.x, p.y);
            if (node == null || node.getTerrain() == ETerrain.Water) {
                continue;
            }
            
            if (!hasTreasure && node.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
                logger.info("Found treasure in discovered area at ({}, {})", p.x, p.y);
                return p;
            }
            
            if (hasTreasure && node.getFortState() == EFortState.EnemyFortPresent) {
                logger.info("Found enemy fort in discovered area at ({}, {})", p.x, p.y);
                return p;
            }
        }
        
        return null;
    }


    /**
     * 
     * Checks a mountain field if there are any objectives, skips any water fields and updates the probabilityMap and discovered fields
     * 
     * @param here The mountain the player is currently on 
     * @param hasTreasure
     * @return Returns the direction of the next move if there is any immediate objective, null else
     */
    public Point checkMountainVisibility(Point here, boolean hasTreasure) {
           
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                
                int nx = here.x + dx;
                int ny = here.y + dy;
                
                if (nx < 0 || nx > map.getXDimension() || ny < 0 || ny > map.getYDimension()) {
                	continue;
                }
                
                if (!ValidateCoordinate.checkCoordinate(nx, ny, map)) continue;
                
                Point neighbor = new Point(nx, ny);
                FullMapNode node = map.getMapNode(nx, ny);
                
                if (node == null || node.getTerrain() == ETerrain.Water) {
                    continue;
                }

                discovered.add(neighbor);
                probMap.ruleOut(neighbor);
                
                if (!hasTreasure && node.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
                    logger.info("Found treasure at ({}, {})!", nx, ny);
                    if (ValidateCoordinate.checkCoordinate(neighbor.x, neighbor.y, map)) {
                    	return neighbor;
                    }
                }
                
                if (hasTreasure && node.getFortState() == EFortState.EnemyFortPresent) {
                	logger.info("Found enemy fort at ({}, {})!", nx, ny);
                    if (ValidateCoordinate.checkCoordinate(neighbor.x, neighbor.y, map)) {
                    	return neighbor;
                    }
                }
            }
        }
        
        return null;
    }
	
	
}
