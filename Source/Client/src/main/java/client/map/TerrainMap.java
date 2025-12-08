package client.map;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromserver.FullMapNode;

public class TerrainMap {
	private final static Logger logger = LoggerFactory.getLogger(TerrainMap.class);

	private final FullMapNode[][] map;
	private final int maxX;
	private final int maxY;

	/**
	 * Generates a 2-D Array of the server side FullMap for better iteration and
	 * accessibility
	 * 
	 * @param nodes
	 */
	public TerrainMap(Collection<FullMapNode> nodes) {

		logger.info("Constructing the local copy of FullMap");

		int maxXTemp = 0;
		int maxYTemp = 0;

		for (FullMapNode node : nodes) {
			if (node.getX() > maxXTemp) {
				maxXTemp = node.getX();
			}
			if (node.getY() > maxYTemp) {
				maxYTemp = node.getY();
			}
		}

		this.maxX = maxXTemp + 1;
		this.maxY = maxYTemp + 1;
		map = new FullMapNode[maxX][maxY];

		for (FullMapNode node : nodes) {
			map[node.getX()][node.getY()] = node;
		}
		logger.info("The local FullMap has a dimension of X: {} and Y: {}", maxX, maxY);

	}

	public FullMapNode getMapNode(int x, int y) {
		if (x < 0 || x >= maxX || y >= maxY || y < 0) {
			return null;
		}
		return map[x][y];
	}

	public FullMapNode[][] getMap() {
		return this.map;
	}

	public Stream<FullMapNode> stream() {
		Set<FullMapNode> mapNodes = new HashSet<>();
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				FullMapNode node = map[x][y];
				if (node != null) {
					mapNodes.add(node);
				}
			}
		}
		return mapNodes.stream();

	}

	public int getXDimension() {
		return this.maxX;
	}

	public int getYDimension() {
		return this.maxY;
	}
}
