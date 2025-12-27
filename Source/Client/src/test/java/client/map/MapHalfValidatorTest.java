package client.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import client.map.placers.MapNode;
import client.map.validation.MapValidator;
import client.map.validation.ValidationResult;
import messagesbase.messagesfromclient.ETerrain;

public class MapHalfValidatorTest {

	private static final int WIDTH = 10;
	private static final int HEIGHT = 5;

	/**
	 * Helper method to count the fields with terrain water that are on the edges of
	 * the half map
	 * 
	 * @param map
	 * @return
	 */
	private int countWaterFieldsOnEdges(MapNode[][] map) {
		int countWaterFieldsOnEdges = 0;

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1) {
					if (map[x][y].getTerrain() == ETerrain.Water) {
						countWaterFieldsOnEdges++;
					}
				}
			}
		}
		return countWaterFieldsOnEdges;

	}

	@Test
	void countWaterFieldsOnEdges_shouldReturnIfMoreThanTwoWaterFieldsAreOnEdges() {
		MapNode[][] map = new FirstMapHalfGenerator().getMap();
		int waterFields = countWaterFieldsOnEdges(map);
		Assertions.assertTrue(waterFields >= 2,
				"Expected: There are a maximum of 2 water fields on the edge, actual amount: " + waterFields);
	}

	@Test
	void castlePlacementIsValid_shouldReturnValidCastlePlacement() {
		MapNode[][] map = new FirstMapHalfGenerator().getMap();
		int castleCount = 0;
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (map[x][y].hasCastle()) {
					castleCount++;
					Assertions.assertEquals(ETerrain.Grass, map[x][y].getTerrain(), "Castle position is not valid");
				}
			}
		}
		Assertions.assertEquals(1, castleCount, "There is an invalid amount of castles on the map");
	}

	@Test
	void mapHalfIsValid_shouldReturnTrueWhenMapGeneratedCorrectly() {
		MapNode[][] map = new FirstMapHalfGenerator().getMap();
		MapValidator mapValidator = new MapValidator(map);
		ValidationResult result = mapValidator.isValidMap(); 
		Assertions.assertTrue(result.getIsValidMap() == true,
				"Map is not valid due to an invalid field on the generated half map");

	}

	@Test
	void mapHalfIsValid_shouldReturnFalseWhenMapGeneratedIncorrectly() {
		MapNode[][] map = new FirstMapHalfGenerator().getMap();
		MapValidator mapValidator = new MapValidator(map);
		ValidationResult result = mapValidator.isValidMap(); 
		Assertions.assertTrue(result.getIsValidMap() == false,
				"Map is not valid due to an invalid field on the generated half map");

	}


}
