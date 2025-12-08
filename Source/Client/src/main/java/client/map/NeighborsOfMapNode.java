package client.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import client.movement.ValidateCoordinate;
import messagesbase.messagesfromclient.ETerrain;

/**
 * Helper class to return the neighbors of a node.
 */
public class NeighborsOfMapNode {

	private static final int[][] DIR = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

	private MapNode[][] map;

	public NeighborsOfMapNode(MapNode[][] map) {
		this.map = map;
	}

	public List<Point> getNeighbors(Point point) {
		List<Point> list = new ArrayList<>(4);
		for (int[] dir : DIR) {
			int nextX = point.x + dir[0];
			int nextY = point.y + dir[1];

			if (!ValidateCoordinate.checkCoordinateOfMapHalf(nextX, nextY, map)) {
				continue;
			}

			if (map[nextX][nextY].getTerrain() == ETerrain.Water)
				continue;

			list.add(new Point(nextX, nextY));
		}
		return list;
	}

}
