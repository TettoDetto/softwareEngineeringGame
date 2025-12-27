package client.movement.model;

import java.awt.Point;

import client.map.TerrainMap;
import messagesbase.messagesfromserver.FullMapNode;

public class TerrainCost {


	/**
	 * Helper class to return the cost of a given terrain field on given
	 * coordinates.
	 * 
	 * @param map
	 */


    public double getTerrainCost(TerrainMap map, Point p) {
        FullMapNode node = map.getMapNode(p.x, p.y);
        
        if (node == null) {
            return Double.POSITIVE_INFINITY;
        }
        
        switch (node.getTerrain()) {
            case Grass:
                return 1.0;
            case Mountain:
                return 3.0;
            case Water:
                return Double.POSITIVE_INFINITY;
            default:
                return Double.POSITIVE_INFINITY;
        }
    }

}
