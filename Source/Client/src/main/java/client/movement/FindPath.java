package client.movement;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.utility.IPlayerPosition;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMapNode;


public class FindPath {

    private static final Logger logger = LoggerFactory.getLogger(FindPath.class);
    
    private TerrainMap map;
    private final Set<Point> visited;
    private final Set<Point> discovered;
    private final ProbabilityMap probMap;
    private IMovementContext movementContext;
    private Deque<Point> currentPath;
	private VisibilityGain visibilityGain;
	private TargetSearch targetSearch;
    private Djikstra djikstra;
    private MapLayout mapLayout; 

    /**
     * 
     * The main controller of the pathfinding algorithm. Initializes all necessary fields via movementContext to keep them permanent across game loops
     * 
     * @param movementContext Helper class for holding necessary fields 
     */
    public FindPath(IMovementContext movementContext) {
    	this.movementContext = movementContext;
    	
        IPlayerPosition spawn = movementContext.getPlayerPosition();
        Point playerPos = spawn.playerLocation();
        
        logger.info("Player spawned at [{}, {}]", playerPos.x, playerPos.y);
        
        this.map = movementContext.getTerrainMap();
        this.visited = movementContext.getVisited();
        this.discovered = movementContext.getDiscovered();
        this.probMap = movementContext.getProbabilityMap();
        this.currentPath = new ArrayDeque<>();
        this.mapLayout = new MapLayout(map, playerPos);
            
        this.djikstra = new Djikstra();
    }

    /**
     * 
     * Get's the next move by first checking any immediate objectives if on a mountain, then for visible objectives, if they have been uncovered already. 
     * If none were uncovered, it checks the next move in the currentPath queue, or creates a new path.
     * If the new path queue doesn't hold any moves, it returns the fallback move.  
     * 
     * @param hasTreasure
     * @param position Current position of the player
     * @return Returns the next move 
     */
    public String nextMove(boolean hasTreasure, IPlayerPosition position) {
        this.map = movementContext.getTerrainMap();
        this.visibilityGain = new VisibilityGain(map, discovered, probMap);
        this.targetSearch = new TargetSearch(map, mapLayout, visited, visibilityGain, probMap);

        Point here = position.playerLocation();
        logger.info("Determining next move from ({}, {})", here.x, here.y);
        
        if (!visited.contains(here)) {
            visited.add(here);
            discovered.add(here);
            probMap.ruleOut(here);
        }
        
        
        Point immediateObjective = visibilityGain.checkMountainVisibility(here, hasTreasure);
        if (immediateObjective != null) {
            logger.info("Found objective from a mountain at ({}, {}), creating path", immediateObjective.x, immediateObjective.y);
            currentPath = djikstra.djikstraPath(here, immediateObjective, map);
            Point next = currentPath.removeFirst();
            logger.info("Moving onto point ({}, {}) with terrain: {}",next.x, next.y, map.getMapNode(next.x, next.y).getTerrain());
            return Direction.getDirection(here, next);
        }
        
        
        Point visibleObjective = visibilityGain.scanForVisibleObjective(hasTreasure);
        if (visibleObjective != null && !visibleObjective.equals(here)) {
            logger.info("Moving towards visible objective at ({}, {})", visibleObjective.x, visibleObjective.y);
            currentPath = djikstra.djikstraPath(here, visibleObjective, map);
            if (!currentPath.isEmpty()) {
                Point next = currentPath.removeFirst();
                logger.info("Moving onto terrain: {}", map.getMapNode(next.x, next.y).getTerrain());
                if (ValidateCoordinate.checkCoordinate(next.x, next.y, map)) {
                	return Direction.getDirection(here, next);
                }
            }
        }
        
        
        if (currentPath.isEmpty() || currentPath.peekFirst().equals(here)) {
            if (!currentPath.isEmpty()) {
                currentPath.removeFirst();
            }
            planNewPath(here, hasTreasure);
        }
        
       
        if (!currentPath.isEmpty()) {
            Point next = currentPath.removeFirst();
            if (ValidateCoordinate.checkCoordinate(next.x, next.y, map)) {
            	return Direction.getDirection(here, next);
            }
        }
        
        
        return fallbackMove(here);
    }


    /**
     * 
     * Plans a new path with djikstras algorithm and adds it to the currentPath queue.
     * 
     * @param start Start point, from which to plan
     * @param hasTreasure
     */
    private void planNewPath(Point start, boolean hasTreasure) {
        logger.info("Planning new path from ({}, {})", start.x, start.y);
        
        Point target = targetSearch.findBestTarget(start, hasTreasure);
        
        if (target != null && !target.equals(start)) {
            logger.info("Target found at ({}, {})", target.x, target.y);
            currentPath = djikstra.djikstraPath(start, target, map);
        } else {
            logger.warn("No valid target found, exploring randomly");
            currentPath.clear();
        }
    }

  
    /**
     * 
     * Get's all neighbours of the current node, that also means neighbours diagonally to the current node 
     * 
     * @param p The current node
     * @return Returns a list of neighbouring nodes
     */
    private List<Point> getNeighbors(Point p) {
        List<Point> neighbors = new ArrayList<>();
        
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        for (int[] dir : directions) {
            int nx = p.x + dir[0];
            int ny = p.y + dir[1];
            
            if (ValidateCoordinate.checkCoordinate(nx, ny, map)) {
                neighbors.add(new Point(nx, ny));
            }
        }
        
        return neighbors;
    }
	
    /**
     * 
     * As a last resort, this methods is called, to generate a random move in any direction, that leads onto a valid field
     * 
     * @param here Current point
     * @return Returns a valid move in a random direction
     */
    private String fallbackMove(Point here) {
        for (Point neighbor : getNeighbors(here)) {
            if (ValidateCoordinate.checkCoordinate(neighbor.x, neighbor.y, map)) {
                return Direction.getDirection(here, neighbor);
            }
        }
        return null;
    }

    /**
     * 
     * Updates the current terrainMap field with a new map, to always have a current version of the map.
     * 
     * @param newMap
     */
    public void updateTerrainMap(TerrainMap newMap) {
        if (newMap == null) {
            return;
        }
        
        this.map = newMap;
        
        for (Point visitedPos : visited) {
            FullMapNode node = map.getMapNode(visitedPos.x, visitedPos.y);
            
            if (node != null && node.getTerrain() == ETerrain.Mountain) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;
                        
                        int nx = visitedPos.x + dx;
                        int ny = visitedPos.y + dy;
                        
                        if (ValidateCoordinate.checkCoordinate(nx, ny, map)) {
                            Point neighbor = new Point(nx, ny);
                            FullMapNode neighborNode = map.getMapNode(nx, ny);
                            
                            if (neighborNode != null && neighborNode.getTerrain() != ETerrain.Water) {
                                discovered.add(neighbor);
                            }
                        }
                    }
                }
            }
        }
        
        currentPath.clear();
    }

}