package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.map.MapHalfGenerator;
import client.map.MapValidator;
import client.network.Network;

public class MainHalfMapSending {

	final Logger logger = LoggerFactory.getLogger(MainHalfMapSending.class);

	/**
	 * Used for creating, validating and sending a HalfMap
	 * 
	 * 
	 * @param currentNetwork The network instance used when starting the game. Used to send the map over the network to the server
	 */
	public MainHalfMapSending(Network currentNetwork) {
		final int MAX_ATTEMPTS = 10;

		for (int i = 0; i < MAX_ATTEMPTS; i++) {

			MapHalfGenerator mapHalf = new MapHalfGenerator();
			MapValidator validator = new MapValidator(mapHalf.getMap());
			boolean isValidMap = validator.isValidMap();

			if (isValidMap) {
				currentNetwork.sendMapHalf(mapHalf);
				logger.info("Sent half map, moving to movement!");
				break;

			} else {
				logger.error("The map creator made a mistake while creating. The map has to be redone!");
				continue;
			}
		}
	}
}
