package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.cli.CliController;
import client.cli.CliModel;
import client.cli.CliView;
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
		
		CliModel model = new CliModel();
		CliController controller = new CliController(model);
		CliView view = new CliView(model, controller);
		String firstName = "Benedikt";
		String lastName = "Adler";
		String uName = "adlerb88";
		
		try {

			controller.handleGameStart();
			
			Network currentNetwork = new Network(args[1], args[2]);
			currentNetwork.registerPlayer(firstName, lastName, uName);
			
			GameController loop = new GameController(currentNetwork, controller);
	
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
