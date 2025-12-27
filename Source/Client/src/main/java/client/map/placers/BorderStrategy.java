package client.map.placers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

public class BorderStrategy {

	private MapNode[][] halfMap;
	private FullMap fullMap;
	private List<FullMapNode> borderNodes = new ArrayList<>();
	private final static Logger logger = LoggerFactory.getLogger(BorderStrategy.class);
	private static final int WIDTH = 10;
	private static final int HEIGHT = 5;

	public BorderStrategy(Set<EBorder> connectingBorder, FullMap fullMap, MapNode[][] halfMap) {
		
		this.halfMap = halfMap;
		this.fullMap = fullMap;
		
		if (connectingBorder.contains(EBorder.Bottom)) {
			logger.info("Matching our bottom corner to the top corner of the first map half");
			matchTheirTopToOurBottomBorder();
		}
		
		if (connectingBorder.contains(EBorder.Left)) {
			logger.info("Matching our left and bottom corner to the to the right and top corner of the first map half");
			matchTheirRightToOurLeftBorder();
		}
		
		if (connectingBorder.contains(EBorder.Right)) {
			logger.info("Matching our right corner to the left corner of the first map half");
			matchTheirLeftToOurRightBorder();
		}
		
		if (connectingBorder.contains(EBorder.Top)) {
			logger.info("Matching our top corner to the bottom corner of the first map half");
			matchTheirBottomToOurTopBorder();
		}
		
	}

	private void matchTheirTopToOurBottomBorder() {
		borderNodes.clear();
		logger.info("Matching bottom corner");
		
		int borderCoordinate = 4; 
		
		for (FullMapNode node : fullMap) {
			if (node != null && node.getY() == borderCoordinate) {
				borderNodes.add(node);
				
			}
		}
		
		borderNodes.sort(Comparator.comparingInt(FullMapNode::getX));
		
		for (FullMapNode node : borderNodes) {
			int firstMapHalfX = node.getX();
			int secondMapHalfX = firstMapHalfX;
			int secondMapHalfY = 0;
			logger.info("Looking at node [{},{}] on first map half", node.getX(), node.getY());
			logger.info("Terrain of node [{},{}] before adjusting: {}, after adjusting {}", secondMapHalfX, secondMapHalfY, halfMap[secondMapHalfX][secondMapHalfY].getTerrain(), node.getTerrain());
			adjustBorderTerrain(secondMapHalfX, secondMapHalfY, node.getTerrain());
		}
	}

	private void matchTheirLeftToOurRightBorder() {
		
		logger.info("Matching right corner");
		
		int borderCoordinate = 10;
		
		for (FullMapNode node : fullMap) {
			if (node != null && node.getX() == borderCoordinate) {
				borderNodes.add(node);
				
			}
		}
		borderNodes.sort(Comparator.comparingInt(FullMapNode::getY));
		
		for (FullMapNode node : borderNodes) {
			int firstMapHalfY = node.getY();
			int secondMapHalfX = WIDTH-1;
			int secondMapHalfY = firstMapHalfY;
			logger.info("Terrain of node [{},{}] before adjusting: {}, after adjusting {}", secondMapHalfX, secondMapHalfY, halfMap[secondMapHalfX][secondMapHalfY], node.getTerrain());
			adjustBorderTerrain(secondMapHalfX, secondMapHalfY, node.getTerrain());
		}
	}

	private void matchTheirBottomToOurTopBorder() {
		
		logger.info("Matching top corner");
		
		int borderCoordinate = 5;
		
		for (FullMapNode node : fullMap) {
			if (node != null && node.getY() == borderCoordinate) {
				borderNodes.add(node);
				
			}		
		}
		
		borderNodes.sort(Comparator.comparingInt(FullMapNode::getX));
		
		for (FullMapNode node : borderNodes) {
			int firstMapHalfX = node.getX();
			int secondMapHalfX = firstMapHalfX;
			int secondMapHalfY = HEIGHT-1;
			logger.info("Terrain of node [{},{}] before adjusting: {}, after adjusting {}", secondMapHalfX, secondMapHalfY, halfMap[secondMapHalfX][secondMapHalfY], node.getTerrain());
			adjustBorderTerrain(secondMapHalfX, secondMapHalfY, node.getTerrain());
		}
	}

	private void matchTheirRightToOurLeftBorder() {
		borderNodes.clear();
		logger.info("Matching left corner");
		
		int borderCoordinate = 9;
		
		for (FullMapNode node : fullMap) {
			if (node != null && node.getX() == borderCoordinate) {
				borderNodes.add(node);
			}
		}
		
		borderNodes.sort(Comparator.comparingInt(FullMapNode::getY));
		
		for (FullMapNode node : borderNodes) {
			int firstMapHalfY = node.getY();
			int secondMapHalfX = 0;
			int secondMapHalfY = firstMapHalfY;
			logger.info("Looking at node [{},{}] on first map half", node.getX(), node.getY());
			logger.info("Terrain of node [{},{}] before adjusting: {}, after adjusting {}", secondMapHalfX, secondMapHalfY, halfMap[secondMapHalfX][secondMapHalfY].getTerrain(), node.getTerrain());
			adjustBorderTerrain(secondMapHalfX, secondMapHalfY, node.getTerrain());
		}
	}
	
	private void adjustBorderTerrain(int x, int y, ETerrain terrain) {
		
		if (halfMap[x][y].hasCastle()) {
			logger.info("Map node [{}, {}] contains our castle", x, y);
			return;
		}
		
		halfMap[x][y].setTerrain(terrain);
		logger.info("Set terrain at [{}, {}] to {} as on the other side of the border",x, y, terrain);
	}
	
	
	public MapNode[][] getNewMap() {
		return halfMap;
	}
	
}
