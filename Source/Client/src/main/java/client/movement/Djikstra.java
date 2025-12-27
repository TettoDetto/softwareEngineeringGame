package client.movement;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import client.map.TerrainMap;
import client.movement.model.TerrainCost;
import client.movement.model.ValidateCoordinate;

public class Djikstra {
	
	private TerrainMap map;

	 /**
	 * 
	 * Djikstras algorithm used for path finding. Uses a priority queue for internal ordering and maps to keep track of cost and visited fields
	 * 
	 * @param start Specified point from which to start
	 * @param goal Point, to which the client wants to go to
	 * @param map The local client side representation of the server side full map
	 * @return Returns the shortest (lowest cost) path to the goal field as a Queue, so the last element is the next best move to make
	 */
	public Deque<Point> djikstraPath(Point start, Point goal, TerrainMap map) {
		 this.map = map;
	        PriorityQueue<DijkstraNode> openSet = new PriorityQueue<>();
	        Map<Point, Point> cameFrom = new HashMap<>();
	        Map<Point, Double> costSoFar = new HashMap<>();
	        
	        openSet.add(new DijkstraNode(start, 0.0));
	        costSoFar.put(start, 0.0);
	        
	        while (!openSet.isEmpty()) {
	            DijkstraNode current = openSet.poll();
	            
	            if (current.point.equals(goal)) {
	                return reconstructPath(cameFrom, goal);
	            }
	            
	            for (Point neighbor : getNeighbors(current.point)) {
	                if (!ValidateCoordinate.checkCoordinate(neighbor.x, neighbor.y, map)) {
	                    continue;
	                }
	                
	                double newCost = costSoFar.get(current.point) + new TerrainCost().getTerrainCost(map, neighbor);
	                
	                //If we move to a new field we put the neighbor, to which we moved into costSoFar and cameFrom, in order to keep track of the path we made
	                
	                if (newCost < costSoFar.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) { 
	                    costSoFar.put(neighbor, newCost);
	                    cameFrom.put(neighbor, current.point);
	                    
	                    double priority = newCost + manhattanDistance(neighbor, goal);
	                    openSet.add(new DijkstraNode(neighbor, priority));
	                }
	            }
	        }
	        
	        return new ArrayDeque<>();
	    }
	
	/**
	 * 
	 * Since we add the path to the queue in the wrong order, we need to reverse it, to get the lowest cost node in the front
	 * 
	 * @param cameFrom A map with the current node and the neighbor from which we came. 
	 * @param current The current point we are standing on
	 * @return A queue with the path in the correct order
	 */
	public Deque<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
		Deque<Point> path = new ArrayDeque<>();
	        
		while (cameFrom.containsKey(current)) {
			path.addFirst(current);
			current = cameFrom.get(current);
	    }
	        
	    return path;
	}

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
	    
	private double manhattanDistance(Point a, Point b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
    private static class DijkstraNode implements Comparable<DijkstraNode> {
        final Point point;
        final double priority;
        
        DijkstraNode(Point point, double priority) {
            this.point = point;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(DijkstraNode other) {
            return Double.compare(this.priority, other.priority);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DijkstraNode)) return false;
            return this.point.equals(((DijkstraNode) obj).point);
        }
        
        @Override
        public int hashCode() {
            return point.hashCode();
        }
    }
	
}
