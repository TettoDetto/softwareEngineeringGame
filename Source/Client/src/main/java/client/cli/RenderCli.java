package client.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.TerrainMap;
import client.utility.MyPlayerState;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.GameState;

public class RenderCli {
	final Logger logger = LoggerFactory.getLogger(RenderCli.class);

	public static void visualizeCli(GameState gameState) {
		clearCli();
		System.out.print(buildCli(gameState));
	}

	private static StringBuilder buildCli(GameState gameState) {

		TerrainMap fullMap = new TerrainMap(gameState.getMap().getMapNodes());
		int maxX = fullMap.getXDimension();
		int maxY = fullMap.getYDimension();

		StringBuilder outputString = new StringBuilder(maxX * maxY);

		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < maxY; y++) {
				FullMapNode node = fullMap.getMapNode(x, y);
				if (node == null) {
					continue;
				}
				String field = new TerrainToString().toString(node.getTerrain());

				if (node.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition) {
					field = ECliOutput.PLAYER.toString();
				} else if (node.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition) {
					field = ECliOutput.PLAYERANDENEMY.toString();
				} else if (node.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition) {
					field = ECliOutput.ENEMY.toString();
				} else if (node.getFortState() == EFortState.MyFortPresent) {
					field = ECliOutput.FORT.toString();
				} else if (node.getFortState() == EFortState.EnemyFortPresent) {
					field = ECliOutput.ENEMYFORT.toString();
				} else if (node.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
					field = ECliOutput.TREASURE.toString();
				} else if (node.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition
						&& node.getTreasureState() == ETreasureState.MyTreasureIsPresent) {
					field = ECliOutput.PLAYERANDTREAUSRE.toString();
				} //else if (node.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition
//						&& myPlayer.hasTreasure()) {
//					field = ECliOutput.HASTREASURE.toString();
	//			}
				outputString.append(field);
			}
			outputString.append('\n');
			outputString.append('\n');
		}
		return outputString;
	}

	private static void clearCli() {
		System.out.println("\033[2J");
		System.out.flush();

	}

}
