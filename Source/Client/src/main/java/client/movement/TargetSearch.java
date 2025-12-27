package client.movement;

import java.awt.Point;
import java.util.Set;

import client.map.TerrainMap;
import client.movement.model.MapLayout;
import client.movement.model.ValidateCoordinate;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMapNode;

public class TargetSearch {

    private static final double VISITEDPENALTY = 100;
	private static final int NOGAINPENALTY = 500;
	private static final double DISCOVEREDPENALTY = 500;
	private final double VISIBILITY_WEIGHT = 10.0;
    private final double PROBABILITY_WEIGHT = 0.0;
    private final double DISTANCE_WEIGHT = 5.5;
	private TerrainMap map;
	private MapLayout mapLayout;
	private Set<Point> visited;
	private Set<Point> discovered;
	private VisibilityGain visibility;
	private ProbabilityMap probMap;
	
	/**
	 * 
	 * Searches for the next best target using weights and the probability gained from the {@link ProbabilityMap}
	 * 
	 * @param map Client side presentation of the {@link messagesbase.messagesfromserver.FullMap}
	 * @param mapLayout Condition if the map is combined breadth or length wise
	 * @param visited Set of already visited nodes. Kept in the movementContext class
	 * @param visibility Instance of the VisibilityGain class, giving a score to each mountain and how many grass tiles it uncovers
	 * @param probMap Global instance of the probabilityMap
	 */
	public TargetSearch(TerrainMap map, MapLayout mapLayout, Set<Point> visited, Set<Point> discovered, VisibilityGain visibility, ProbabilityMap probMap) {
		this.map = map;
		this.mapLayout = mapLayout;
		this.visited = visited;
		this.discovered = discovered;
		this.visibility = visibility;
		this.probMap = probMap;
	}
    
    
    /**
     * 
     * Finds the best target by going over the map, excluding not viable nodes and if client has the treasure or not
     * 
     * @param start The start point from which the algorithm calculates
     * @param hasTreasure 
     * @return Returns the point of the best target
     */
    public Point findBestTarget(Point start, boolean hasTreasure) {
        boolean searchOwnHalf = !hasTreasure;
        
        Point bestTarget = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        
        for (int x = 0; x < map.getXDimension(); x++) {
            for (int y = 0; y < map.getYDimension(); y++) {
                Point candidate = new Point(x, y);
                
                if (searchOwnHalf != isOwnHalf(candidate)) {
                    continue;
                }
                
                if (!ValidateCoordinate.checkCoordinate(candidate.x, candidate.y, map)) {
                    continue;
                }
                
                double score = evaluateTarget(start, candidate, hasTreasure);
                
                if (score > bestScore) {
                    bestScore = score;
                    bestTarget = candidate;
                }
            }
        }
        
        return bestTarget;
    }

    /**
     * 
     * Method to evaluate a target by including mountain visibility gain, how far it is away, and 
     * the probability of the treasure or castle being there.
     * Excludes any water fields by assigning infinite cost to it.
     * 
     * @param start The start from which the algorithm calculates
     * @param target The potential target to assign a score to
     * @param hasTreasure 
     * @return Returns the score of the target viewed
     */
    public double evaluateTarget(Point start, Point target, boolean hasTreasure) {
        FullMapNode node = map.getMapNode(target.x, target.y);
        double score = 0.0;
        
        if (node == null || node.getTerrain() == ETerrain.Water) {
            return Double.NEGATIVE_INFINITY;
        }
        
        if (visited.contains(target)) {
            score -= VISITEDPENALTY;
        }
        
        if (discovered.contains(target)) {
        	score -= DISCOVEREDPENALTY;
        }
        

        int visibilityGain = visibility.calculateVisibilityGain(target);
        score += visibilityGain * VISIBILITY_WEIGHT;
        
        double probability = hasTreasure 
            ? probMap.getFortProbability(target)
            : probMap.getTreasureProbability(target);
        score += probability * PROBABILITY_WEIGHT;
        
        if (node.getTerrain() == ETerrain.Mountain && visibilityGain == 0 && probability == 0) {
        	return -NOGAINPENALTY;
        }
        
        double distance = manhattanDistance(start, target);
        score -= distance * DISTANCE_WEIGHT;
        
        return score;
    }
    
    /**
     * 
     * Checks if a target point is in the clients own half, based on the map layout and if the client has it's treasure or not
     * 
     * @param p Point to evaluate
     * @return Returns true or false, if it's in the own half or not
     */
    private boolean isOwnHalf(Point p) {
        if (mapLayout.getSplitVertically()) {
            boolean inLeftHalf = p.x < map.getXDimension() / 2;
            return mapLayout.getFortInFirstHalf() ? inLeftHalf : !inLeftHalf;
        } else {
            boolean inTopHalf = p.y < map.getYDimension() / 2;
            return mapLayout.getFortInFirstHalf() ? inTopHalf : !inTopHalf;
        }
    }
    
    private double manhattanDistance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
	
}
