/*package client.cli;

import client.utility.events.EPropertyChangeEventType;
import client.utility.events.IPropertyChangeListener;
import client.utility.events.PropertyChangeEvent;
import client.utility.events.PropertyType;
import client.utility.events.PropertyTypes;
import messagesbase.messagesfromserver.GameState;

public class CliView implements IPropertyChangeListener {

	private CliController cliController;
	
	public CliView(CliModel model, CliController cliController) {
		this.cliController = cliController;
		model.addPropertyChangeListener(this);
	}
	
	public void render(GameState gameState) {
		RenderCli.visualizeCli(gameState);
	}

	@Override
	public void propertyChange(PropertyChangeEvent<?> event) {
		
		PropertyType<?> type = event.getType();
		
		if (type.equals(EPropertyChangeEventType.INFO_MESSAGE)) {
			outputInfo((String) event.getNewValue());
		} 
		
		else if (type.equals(EPropertyChangeEventType.ERROR)) {
			outputError((String) event.getNewValue());
		} 
		
		else if (type.equals(EPropertyChangeEventType.EXCEPTION)) {
			outputException((Throwable) event.getNewValue());
		}
		
		else if (type.equals(EPropertyChangeEventType.GAME_STATE)) {
			render((GameState)event.getNewValue());
		}
	}

	private void outputInfo(String message) {
		System.out.println(message);
	}

	private void outputError(String message) {
		System.out.println(EPropertyChangeEventType.ERROR.toString() + message);
	}

	private void outputException(Throwable exception) {
		System.err.println(EPropertyChangeEventType.EXCEPTION.toString() + exception.getMessage());
		exception.printStackTrace(System.err);
	}

	public void handleGameState(GameState gameState) {
		cliController.updateGameState(gameState);
	}

}
*/