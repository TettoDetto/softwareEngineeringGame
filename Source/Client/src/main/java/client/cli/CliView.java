package client.cli;

import client.main.GameResult;
import client.map.validation.EValidationResults;
import client.utility.events.EPropertyChangeEventType;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.PropertyChangeEvent;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromserver.GameState;

public class CliView implements IPropertyChangeListener {
	
	
	public void render(GameState gameState) {
		RenderCli.visualizeCli(gameState);
	}

	@Override
	public void propertyChange(PropertyChangeEvent<?> event) {
		
		EPropertyChangeEventType type = event.getType();
		
		switch (type) {
			case GAME_STARTED -> outputGameStart();
			case MAP_CREATION_STARTED -> outputMapCreationStarted();
			case MAP_VALIDATION_ERROR -> outputMapValidationError((EValidationResults) event.getNewValue());
			case MAP_SENT_SUCCESSFULLY ->outputMapSending();
			case INFO_MESSAGE -> outputInfo((String) event.getNewValue());
			case GAME_STATE -> render((GameState) event.getNewValue());
			case TREASURE_COLLECTED -> outputTreasureCollected();
			case MAP_UPDATED -> outputInfo("Map updated");
			case MOVE_CALCULATED -> outputMove((EMove) event.getNewValue());
			case WAITING_FOR_OPPONENT -> waitingForOpponent();
			case GAME_WON -> outputGameWon((GameResult)event.getNewValue());
			case GAME_LOST -> outputGameLost((GameResult)event.getNewValue());
			default -> throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	private void outputTreasureCollected() {
		System.out.println(EPropertyChangeEventType.TREASURE_COLLECTED + " The client found the treasure!!");		
	}

	private void outputMapSending() {
		System.out.println(EPropertyChangeEventType.MAP_SENT_SUCCESSFULLY + " The map has been sent and accepted by the server");	
	}

	private void waitingForOpponent() {
		System.out.println(EPropertyChangeEventType.WAITING_FOR_OPPONENT + " Waiting for opponent...");
	}

	private void outputGameStart() {
		System.out.println("===============================");
		System.out.println(EPropertyChangeEventType.GAME_STARTED + " LET THE GAME BEGIN");
		System.out.println("===============================");
		
	}

	private void outputMapValidationError(EValidationResults newValue) {
		System.out.println("===============================");
		System.out.println(EPropertyChangeEventType.MAP_VALIDATION_ERROR.toString() + " The map generation algorithm made a mistake while generation");
		System.out.println(EPropertyChangeEventType.MAP_VALIDATION_ERROR.toString() + " The following error occured: " + newValue.toString());
		System.out.println("===============================");
		System.out.println(EPropertyChangeEventType.MAP_CREATION_STARTED + " Regenerating the map...");
		
	}

	private void outputMove(EMove move) {
		System.out.println(" " + EPropertyChangeEventType.MOVE_CALCULATED.toString() + " Calculated the next move, we move: " + move.toString());		
	}

	private void outputGameWon(GameResult newValue) {
		System.out.println("===============================");
		System.out.println(EPropertyChangeEventType.GAME_WON.toString() + " GAME WON!");
		System.out.println(EPropertyChangeEventType.GAME_WON.toString() + " Treasure collected: " + (newValue.hasTreasure() ? "Yes" : "No"));
		System.out.println(EPropertyChangeEventType.GAME_WON.toString() + " Moves taken: " + newValue.getRounds());
		System.out.println("===============================");
	}

	private void outputGameLost(GameResult newValue) {
		System.out.println("===============================");
		System.out.println(EPropertyChangeEventType.GAME_LOST.toString() + " GAME LOST!");
		System.out.println(EPropertyChangeEventType.GAME_LOST.toString() + " Treasure collected: " + (newValue.hasTreasure() ? "Yes" : "No"));
		System.out.println(EPropertyChangeEventType.GAME_LOST.toString() + " Moves taken: " + newValue.getRounds());
		System.out.println("===============================");
	}

	private void outputInfo(String message) {
		System.out.println(EPropertyChangeEventType.INFO_MESSAGE.toString() + " " + message);
	}
	private void outputMapCreationStarted() {
		System.out.println(EPropertyChangeEventType.MAP_CREATION_STARTED + " The Client is now constructing the half map");
	}

}
