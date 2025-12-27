package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.cli.CliView;
import client.map.MapHalfService;
import client.movement.MovementService;
import client.network.Network;

public class MainClient {

	private final static Logger logger = LoggerFactory.getLogger(MainClient.class);
	
	/*
	 * 
	 * Starts the main loop of the game Initializes all constructs and passes them
	 * onto the map sending part and the movement sending part of the main function
	 * Updates game state after every action sent to the server
	 */

	public static void main(String[] args) {
		
		String firstName = "Benedikt";
		String lastName = "Adler";
		String uName = "adlerb88";
		
		try {
			
			Network currentNetwork = new Network(args[1], args[2]);
			currentNetwork.registerPlayer(firstName, lastName, uName);
			
			GameModel gameStateManager = new GameModel(currentNetwork);
			UtilityModel model = new UtilityModel();
			CliView view = new CliView();
			
			gameStateManager.addPropertyChangeListener(view);
			model.addPropertyChangeListener(view);
			
			MovementService movementService = new MovementService(currentNetwork, currentNetwork.getPlayerId().getUniquePlayerID());
			
			MapHalfService mapHalfService = new MapHalfService(currentNetwork);
			
			GameController loop = new GameController(gameStateManager, model, movementService, mapHalfService);
	
			while (!loop.getFinished()) {
				logger.info("Starting new game loop");
				loop.executeGame();
				logger.info("One game loop finished");
			}
			
		} catch (Exception e) {
			System.err.println("A fatal Error has occured");
			e.printStackTrace();
			System.exit(1);
		}
		
		logger.info("Exiting game");

		System.exit(0);
	}

}
