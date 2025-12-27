//package client.cli;
//
//import client.map.TerrainMap;
//import client.map.validation.EValidationResults;
//import messagesbase.messagesfromclient.EMove;
//import messagesbase.messagesfromserver.GameState;
//
//public class CliController {
//
//	private CliModel model;
//	
//	public CliController(CliModel model) {
//		this.model = model;
//	}
//	
//	public void updateGameState(GameState gameState) {
//		if (gameState == null) {
//			return;
//		}
//		model.updateGameState(gameState);
//	}
//	
//	public void updateTerrainMap(TerrainMap terrainMap) {
//		
//		if (terrainMap == null) {
//			return;
//		}
//		
//		model.updateTerrainMap(terrainMap);
//	}
//
//	public void handleFinished(boolean won, int loopCount, boolean hasTreasure) {
//		if (won) {
//			model.setWon(won, loopCount, hasTreasure);
//		}
//		else {
//			model.setLost(won, loopCount, hasTreasure);	
//		}
//	}
//	public void handleMovement(EMove move) {
//		if (move == null) {
//			return;
//		}
//		model.setMove(move);
//	}
//	public void handleMapSending(boolean value) {
//		model.setSentMap(true);
//	}
//	public void handleWaiting() {
//		model.setWaiting(false);
//	}
//	public void handleMapValidationError(EValidationResults eValidationResults) {
//		model.setMapValidationError(eValidationResults);
//	}
//
//	public void handleMapCreationStarted() {
//		model.setMapCreation();
//	}
//
//	public void handleGameStart() {
//		model.setGameStart();
//	}
//
//	public void handleTresureCollected() {
//		model.setTreasureCollected();
//	}
//	
//}