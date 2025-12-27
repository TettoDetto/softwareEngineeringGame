package client.movement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.map.TerrainMap;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;

public class ProbabilityMapTest {

	private TerrainMap map;
	private ProbabilityMap probMap;
	private Predicate<Point> ownHalf;
	private Predicate<Point> enemyHalf;
	private int WIDTH;
	private int HEIGHT;

	@BeforeEach
	void setUp() {
		WIDTH = 6;
		HEIGHT = 4;

		List<FullMapNode> nodes = new ArrayList<>();

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				ETerrain terrain = (x + y) % 2 == 0 ? ETerrain.Grass : ETerrain.Mountain;
				nodes.add(new FullMapNode(terrain, EPlayerPositionState.NoPlayerPresent,
						ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, x, y));
			}
		}
		map = new TerrainMap(nodes);

		ownHalf = p -> p.x < WIDTH / 2;
		enemyHalf = ownHalf.negate();

		probMap = new ProbabilityMap(map);
	}

	@Test
	void ctor_distributesTreasureOnlyInMyHalf_distributesFortOnlyInEnemyHalf() {
		double sumTreasure = 0;
		double sumFort = 0;
		int treasureFields = 0;
		int fortFields = 0;

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				Point point = new Point(x, y);

				if (map.getMapNode(x, y).getTerrain() == ETerrain.Grass && ownHalf.test(point)) {
					assertThat(probMap.getFortProbability(point), is(equalTo(0.0)));
					sumTreasure += probMap.getTreasureProbability(point);
					treasureFields++;
				} else if (map.getMapNode(x, y).getTerrain() == ETerrain.Grass && enemyHalf.test(point)) {
					assertThat(probMap.getTreasureProbability(point), is(equalTo(0.0)));
					sumFort += probMap.getFortProbability(point);
					fortFields++;
				} else {
					assertThat(probMap.getFortProbability(point), is(equalTo(0.0)));
					assertThat(probMap.getTreasureProbability(point), is(equalTo(0.0)));
				}
			}
		}

		assertThat(sumTreasure, is(closeTo(1.0, 1e-6)));
		assertThat(sumFort, is(closeTo(1.0, 1e-6)));

		assertThat(treasureFields, is(equalTo(6)));
		assertThat(fortFields, is(equalTo(6)));
	}

}
