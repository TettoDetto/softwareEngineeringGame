package client.movement;

import client.map.MapNode;
import client.map.TerrainMap;
import messagesbase.messagesfromclient.ETerrain;

public class ValidateCoordinate {

	/**
	 * Checks if the coordinate is still in bounds
	 * 
	 * @param x
	 * @param y
	 * @param map
	 * @return
	 */
	public static boolean checkCoordinate(int x, int y, TerrainMap map) {
		if (x < 0 || x >= map.getXDimension()) {
			return false;
		}
		if (y < 0 || y >= map.getYDimension()) {
			return false;
		}
		if (map.getMapNode(x, y).getTerrain() == ETerrain.Water) {
			return false;
		}
		return map.getMapNode(x, y) != null;

	}

	public static boolean checkCoordinateOfMapHalf(int x, int y, MapNode[][] map) {

		int width = map.length;
		int height = map[0].length;

		if (x < 0 || x >= width) {
			return false;
		}
		if (y < 0 || y >= height) {
			return false;
		}
		return map[x][y] != null;
	}

}
