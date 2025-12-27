package client.movement;

import java.awt.Point;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.movement.model.MapLayout;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.FullMapNode;

public class ProbabilityMap {

	private final static Logger logger = LoggerFactory.getLogger(ProbabilityMap.class);

	private double pTreasure[][];
	private double pFort[][];
	private final int WIDTH;
	private final int HEIGHT;
	private TerrainMap map;
	private final Predicate<Point> ownHalf;
	private final Predicate<Point> enemyHalf;

	/**
	 * 
	 * The probability Map which represents the terrainMap as a map of probabilities, of where the treasure or castle could be located. 
	 * 
	 * @param map Local client side representation of the server side FullMap
	 * @param ownHalf A test, to check if any point is in the clients own half or not
	 * @param enemyHalf A test, to check if any point is in the clients enemy half or not
	 */
	public ProbabilityMap(TerrainMap map) {
		
		this.map = map;
		this.WIDTH = map.getXDimension();
		logger.info("WIDTH is: {}", WIDTH);
		
		this.HEIGHT = map.getYDimension();
		logger.info("HEIGHT is: {}", HEIGHT);
		
		boolean splitVertically;
		if (map.getXDimension() > map.getYDimension()) {
			splitVertically = true;
		} else {
			splitVertically = false;
		}

		Predicate<Point> ownHalf = p -> splitVertically ? p.x < map.getXDimension() / 2
				: p.y < map.getYDimension() / 2;
		
		Predicate<Point> enemyHalf = ownHalf.negate();
		
		this.ownHalf = ownHalf;
		this.enemyHalf = enemyHalf;
		
		initializeProbabilties();
	}

	private void initializeProbabilties() {
		
		this.pTreasure = new double[map.getXDimension()][map.getYDimension()];
		this.pFort = new double[map.getXDimension()][map.getYDimension()];

		int treasureCount = 0;
		int fortCount = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				FullMapNode node = map.getMapNode(x, y);
				if (node == null || node.getTerrain() != ETerrain.Grass)
					continue;
				Point pt = new Point(x, y);
				if (ownHalf.test(pt))
					treasureCount++;
				if (enemyHalf.test(pt))
					fortCount++;
			}
		}
		logger.info("Own-half grass cells: {}, Enemy-half grass cells: {}", treasureCount, fortCount);

		double initTreasureProb = treasureCount > 0 ? 1.0 / treasureCount : 0;
		double initFortProb = fortCount > 0 ? 1.0 / fortCount : 0;

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				FullMapNode node = map.getMapNode(x, y);
				if (node == null || node.getTerrain() != ETerrain.Grass) {
					pTreasure[x][y] = 0;
					pFort[x][y] = 0;
				} else {
					Point pt = new Point(x, y);
					pTreasure[x][y] = ownHalf.test(pt) ? initTreasureProb : 0;
					pFort[x][y] = enemyHalf.test(pt) ? initFortProb : 0;
				}
			}
		}
		
	}

	public double getTreasureProbability(Point point) {
		return pTreasure[point.x][point.y];
	}

	public double getFortProbability(Point point) {
		return pFort[point.x][point.y];
	}

	/**
	 * 
	 * Rules out a specific point, if there is no treasure or castle on it. 
	 * It renormalizes the whole map without the point, so the probabilities are distributed evenly again
	 * 
	 * @param point Point to rule out
	 */
	public void ruleOut(Point point) {
		if (ownHalf.test(point) && pTreasure[point.x][point.y] > 0) {
			pTreasure[point.x][point.y] = 0;
			renormalize(pTreasure, ownHalf);
		}
		if (enemyHalf.test(point) && pFort[point.x][point.y] > 0) {
			pFort[point.x][point.y] = 0; 
			renormalize(pFort, enemyHalf);
		}
	}

	/**
	 * 
	 * Renormalizes the whole map in order to make the probability add to one again, after a specific point has been ruled out
	 * 
	 * @param array Array of probabilities, either with treasure or castle probabilities
	 * @param region Region of the map, either the own map half (if client doesn't have treasure) or enemy map half (if client has treasure)
	 */
	private void renormalize(double[][] probailityArray, Predicate<Point> region) {
		double sum = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Point pt = new Point(x, y);
				if (!region.test(pt) || !isGrass(x, y))
					continue;
				sum += probailityArray[x][y];
			}
		}
		if (sum <= 0)
			return;
		double factor = 1.0 / sum;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Point pt = new Point(x, y);
				if (region.test(pt) && isGrass(x, y)) {
					probailityArray[x][y] *= factor;
				}
			}
		}
	}

	private boolean isGrass(int x, int y) {
		FullMapNode node = map.getMapNode(x, y);
		return node != null && node.getTerrain() == ETerrain.Grass;
	}

	public void updateTerrainMap(TerrainMap newMap) {
		if (newMap != null) {
			this.map = newMap;
		}
	}

	/**
	 * 
	 * Sets the probability of a specific field having the treasure to one, 
	 * thus making it into a 100% probability, that the field contains the treasure
	 * 
	 * @param p Point for which the probability should be 1
	 */
	public void setTreasureProbability(Point p) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Point pt = new Point(x, y);
				if (ownHalf.test(pt))
					pTreasure[x][y] = 0;
			}
		}
		if (ownHalf.test(p))
			pTreasure[p.x][p.y] = 1.0;
	}
	
	/**
	 * 
	 * Sets the probability of a specific field having the castle to one, 
	 * thus making it into a 100% probability, that the field contains the castle
	 * 
	 * @param p Point for which the probability should be 1
	 */
	public void setFortProbability(Point p) {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Point pt = new Point(x, y);
				if (enemyHalf.test(pt))
					pFort[x][y] = 0;
			}
		}
		if (enemyHalf.test(p))
			pFort[p.x][p.y] = 1.0;
	}

}
