package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.network.Network;

public class MainClient {

	/*
	 * 
	 * Starts the main loop of the game Initializes all constructs and passes them
	 * onto the map sending part and the movement sending part of the main function
	 * Updates game state after every action sent to the server
	 */

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(MainClient.class);

		Network currentNetwork = new Network(args[1], args[2]);
		currentNetwork.registerPlayer("Benedikt", "Adler", "adlerb88");


		GameController loop = new GameController(currentNetwork);

		try {

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
