package client.movement;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import client.map.TerrainMap;
import client.utility.IPlayerPosition;

public class MovementContext implements IMovementContext {

	private Set<Point> visited;
	private Set<Point> discovered;
	private TerrainMap terrainMap;
	private IPlayerPosition playerPosition;
	private ProbabilityMap probMap;
	private boolean hasTreasure;
	
	/**
	 * 
	 * Helper class to store the objects used in findPath. This is also instantiated at the beginning of the game, to keep track of visited and discovered fields
	 * 
	 */
	public MovementContext() {
		this.visited = new HashSet<Point>();
		this.discovered = new HashSet<Point>();
		this.terrainMap = null;
		this.playerPosition = null;
		this.probMap = null;
		this.hasTreasure = false;		
	}

	@Override
	public void setVisited(Set<Point> visited) {
		this.visited = visited;
		
	}

	@Override
	public void setDiscovered(Set<Point> discovered) {
		this.discovered = discovered;
		
	}

	@Override
	public void setTerrainMap(TerrainMap terrainMap) {
		this.terrainMap = terrainMap;
		
	}

	@Override
	public void setPlayerPosition(IPlayerPosition playerPosition) {
		this.playerPosition = playerPosition;
		
	}

	@Override
	public void setProbabilityMap(ProbabilityMap probMap) {
		if (this.probMap == null) {
			this.probMap = probMap;
		}
		
	}
	
	@Override
	public void setHasTreasure(boolean hasTreasure) {
		this.hasTreasure = hasTreasure;
	}

	@Override
	public Set<Point> getVisited() {
		return this.visited;
	}

	@Override
	public Set<Point> getDiscovered() {
		return this.discovered;
	}

	@Override
	public TerrainMap getTerrainMap() {
		return this.terrainMap;
	}

	@Override
	public IPlayerPosition getPlayerPosition() {
		return playerPosition;
	}

	@Override
	public ProbabilityMap getProbabilityMap() {
		return probMap;
	}
	
	@Override
	public boolean getHasTreasure() {
		return this.hasTreasure;
	}


}
