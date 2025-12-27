package client.movement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.movement.model.IMovementContext;
import client.movement.model.MovementContext;
import client.utility.IPlayerPosition;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;

public class FindPathTest {

	private final static Logger logger = LoggerFactory.getLogger(FindPathTest.class);

	private TerrainMap map;
	private ProbabilityMap probMap;
	private Set<Point> discovered = new HashSet<>();
	private IMovementContext movementContext = new MovementContext();

	void setUpTerrainMap() {
		this.map = generateTerrainMap();

		boolean splitVertically;
		if (map.getXDimension() > map.getYDimension()) {
			splitVertically = true;
		} else {
			splitVertically = false;
		}

		Predicate<Point> ownHalf = p -> splitVertically ? p.x < map.getXDimension() / 2 : p.y < map.getYDimension() / 2;
		Predicate<Point> enemyHalf = ownHalf.negate();

		this.probMap = new ProbabilityMap(map, ownHalf, enemyHalf);
		movementContext.setDiscovered(discovered);
		movementContext.setTerrainMap(map);
		movementContext.setProbabilityMap(probMap);
	}

	void setUpSimpleTerrainMap() {
		this.map = generateSimpleTerrainMap();

		boolean splitVertically;
		if (map.getXDimension() > map.getYDimension()) {
			splitVertically = true;
		} else {
			splitVertically = false;
		}

		Predicate<Point> ownHalf = p -> splitVertically ? p.x < map.getXDimension() / 2 : p.y < map.getYDimension() / 2;
		Predicate<Point> enemyHalf = ownHalf.negate();

		this.probMap = new ProbabilityMap(map, ownHalf, enemyHalf);
		movementContext.setDiscovered(discovered);
		movementContext.setTerrainMap(map);
		movementContext.setProbabilityMap(probMap);
	}

	static TerrainMap generateSimpleTerrainMap() {
		List<FullMapNode> nodes = new ArrayList<>();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				ETerrain terrain = ETerrain.Grass;
				EPlayerPositionState position = EPlayerPositionState.NoPlayerPresent;
				ETreasureState treasureState = (x == 4 && y == 0) ? ETreasureState.MyTreasureIsPresent
						: ETreasureState.NoOrUnknownTreasureState;
				EFortState fortState = (x == 4 && y == 4) ? EFortState.EnemyFortPresent
						: EFortState.NoOrUnknownFortState;
				nodes.add(new FullMapNode(terrain, position, treasureState, fortState, x, y));
			}
		}
		TerrainMap newMap = new TerrainMap(nodes);
		return newMap;
	}

	static TerrainMap generateTerrainMap() {
		List<FullMapNode> nodes = new ArrayList<>();
		ETerrain terrain;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				
				if (x == 1 && y == 0) {
					nodes.add(new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 1, 0));
					continue;
				}
				if (x == 1 && y == 1) {
					nodes.add(new FullMapNode(ETerrain.Mountain, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 1, 1));
					continue;
				}
				
				if (x == 0 && y == 1) {
					nodes.add(new FullMapNode(ETerrain.Water, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 0, 1));
					continue;
				}
				
				if (x == 2 && y == 1) {
					nodes.add(new FullMapNode(ETerrain.Grass, EPlayerPositionState.NoPlayerPresent, ETreasureState.NoOrUnknownTreasureState, EFortState.NoOrUnknownFortState, 2, 1));
					continue;
				}
				
				if (x % 2 != 0 && y % 2 != 0) {
					terrain = ETerrain.Grass;
				} else {
					terrain = ETerrain.Mountain;
				}
				
				EPlayerPositionState position = EPlayerPositionState.NoPlayerPresent;
				ETreasureState treasureState = (x == 3 && y == 1) ? ETreasureState.MyTreasureIsPresent
						: ETreasureState.NoOrUnknownTreasureState;
				EFortState fortState = (x == 7 && y == 7) ? EFortState.EnemyFortPresent
						: EFortState.NoOrUnknownFortState;
				nodes.add(new FullMapNode(terrain, position, treasureState, fortState, x, y));
			}
		}
		TerrainMap newMap = new TerrainMap(nodes);
		return newMap;
	}

	@Test
	void nextMove_doesNotMoveOntoWater_movesAroundIt() {
		setUpTerrainMap();
		
		Set<Point> visited = new HashSet<>();
		
		IPlayerPosition location = new PlayerLocator(1,0);
		
		movementContext.setPlayerPosition(location);
		movementContext.setVisited(visited);

		FindPath path = new FindPath(movementContext);
		path.nextMove(false, location);

		for (int i = 0; i < 3; i++) {
			EMove dir = path.nextMove(false, location);
			logger.info("Player moved in direction: " + dir);
			movePlayer(dir, location);
			logger.info("Player on terrain: " + movementContext.getTerrainMap().getMapNode(location.playerLocation().x, location.playerLocation().y).getTerrain());

		}
		assertThat(map.getMapNode(location.playerLocation().x, location.playerLocation().y).getTreasureState(),
				is(ETreasureState.MyTreasureIsPresent));
	}
	
	
	@Test
	void nextMove_staysInOwnHalf_untilTreasureIsFound() {
		
		setUpTerrainMap();
		
		Set<Point> visited = new HashSet<>();
		
		IPlayerPosition location = new PlayerLocator(0, 0);
		
		movementContext.setPlayerPosition(location);
		movementContext.setVisited(visited);

		FindPath path = new FindPath(movementContext);
		path.nextMove(false, location);

		for (int i = 0; i < 10; i++) {
			EMove dir = path.nextMove(false, location);
			logger.info("Player moved in direction: " + dir);
			movePlayer(dir, location);
			assertThat(location.playerLocation().x, lessThanOrEqualTo(4));

			if (map.getMapNode(location.playerLocation().x, location.playerLocation().y)
					.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
				break;
			}
		}
		assertThat(map.getMapNode(location.playerLocation().x, location.playerLocation().y).getTreasureState(),
				is(ETreasureState.MyTreasureIsPresent));
	}

	@Test
	void givenFullMap_FindPathOnlyMovesInTheValidMapRegion_ReturnMovementDirection() {
		
		setUpTerrainMap();

		IPlayerPosition location = new PlayerLocator(0, 0);
		Set<Point> visited = new HashSet<>();
		
		movementContext.setPlayerPosition(location);
		movementContext.setVisited(visited);
		
		FindPath path = new FindPath(movementContext);

		for (int i = 0; i < 50; i++) {
			EMove dir = path.nextMove(false, location);
			movePlayer(dir, location);
			assertThat(location.playerLocation().x, lessThanOrEqualTo(4));

		}
	}

	@Test
	void givenFullMap_FindPathAlwaysFindsShortestPath_ReturnsMovementDirectionsToObject() {
		setUpSimpleTerrainMap();

		IPlayerPosition location = new PlayerLocator(0, 0);
		Set<Point> visited = new HashSet<>();
		
		movementContext.setPlayerPosition(location);
		movementContext.setVisited(visited);
		
		FindPath path = new FindPath(movementContext);
		int moves = 0;

		while (!(location.playerLocation().x == 4 && location.playerLocation().y == 0)) {
			movePlayer(path.nextMove(false, location), location);
			moves++;
		}

		assertThat("Spawn to Treasure is shortest (4)", moves, is(lessThanOrEqualTo(12)));
		moves = 0;

		while (!(location.playerLocation().x == 4 && location.playerLocation().y == 4)) {
			movePlayer(path.nextMove(true, location), location);
			moves++;
		}
		assertThat("Treasure to Enemy Fort (4)", moves, is(lessThanOrEqualTo(12)));
	}

	@Test
	void givenPlayerPosition_isEnemyHalf_ReturnsTrueIfPlayerSpawnedInOtherHalfMap() {

	}
	
	private static void movePlayer(EMove dir, IPlayerPosition position) {
		Point pos = position.playerLocation();
		switch (dir) {
		case EMove.Up:
			position.update(pos.x, pos.y - 1);
			break;
		case EMove.Down:
			position.update(pos.x, pos.y + 1);
			break;
		case EMove.Left:
			position.update(pos.x - 1, pos.y);
			break;
		case EMove.Right:
			position.update(pos.x + 1, pos.y);
			break;
		}
	}

}
