package client.movement.model;

import java.awt.Point;
import java.util.Set;

import client.map.TerrainMap;
import client.movement.ProbabilityMap;
import client.utility.IPlayerPosition;

public interface IMovementContext {

	public void setVisited(Set<Point> visited);
	public void setDiscovered(Set<Point> discovered);
	public void setTerrainMap(TerrainMap terrainMap);
	public void setPlayerPosition(IPlayerPosition playerPosition);
	public void setProbabilityMap(ProbabilityMap probMap);
	public void setHasTreasure(boolean hasTreasure);
	
	public Set<Point>getVisited();
	public Set<Point> getDiscovered();
	public TerrainMap getTerrainMap();
	public IPlayerPosition getPlayerPosition();
	public ProbabilityMap getProbabilityMap();
	public boolean getHasTreasure();
	
}
