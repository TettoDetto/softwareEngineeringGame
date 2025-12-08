package client.map;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import messagesbase.messagesfromclient.ETerrain;

public class MapHalfGeneratorTest {

	private static final int WIDTH = 10;
	private static final int HEIGHT = 5;
	private static final int TOTAL = WIDTH * HEIGHT;
	private static final int amountOfGrass = (int) (TOTAL * 0.48);
	private static final int amountOfWater = (int) (TOTAL * 0.14);
	private static final int amountOfMountain = (int) (TOTAL * 0.10);

	/**
	 * Helper method to map the fields of the HalfMap
	 * 
	 * @param map
	 * @return
	 */
	private Map<ETerrain, Integer> countFields(MapNode[][] map) {
		Map<ETerrain, Integer> count = new EnumMap<>(ETerrain.class);
		for (ETerrain t : ETerrain.values())
			count.put(t, 0);

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				count.merge(map[x][y].getTerrain(), 1, Integer::sum);
			}
		}
		return count;
	}

	@Test
	void sizeOfMapIsTheSame_shouldReturnDimensionOfMap() {
		new MapHalfGenerator();
		MapNode[][] map = new MapHalfGenerator().getMap();
		Assertions.assertEquals(WIDTH, map.length, "Width of Map incorrect");
		Assertions.assertEquals(HEIGHT, map[0].length, "Height of Map incorrect");
		Assertions.assertEquals(TOTAL, WIDTH * HEIGHT, "Total number of fields of Map incorrect");
	}

	@RepeatedTest(10)
	void numberOfTerrainTypes_shouldReturnCorrectAmountOfTerrainTypes() {
		new MapHalfGenerator();
		MapNode[][] map = new MapHalfGenerator().getMap();
		Map<ETerrain, Integer> count = countFields(map);

		Assertions.assertTrue(count.get(ETerrain.Water) >= amountOfWater,
				"The amount of water fields doesnt't match the requirement");
		Assertions.assertTrue(count.get(ETerrain.Grass) >= amountOfGrass,
				"The amount of grass fields doesnt't match the requirement");
		Assertions.assertTrue(count.get(ETerrain.Mountain) >= amountOfMountain,
				"The amount of mountain fields doesnt't match the requirement");

		double sumOfFields = count.values().stream().mapToInt(Integer::intValue).sum();
		Assertions.assertEquals(TOTAL, sumOfFields, "The sum of fields on the half map doesn't match the requirement");
	}

}
