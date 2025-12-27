package client.movement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.model.IMovementContext;
import messagesbase.messagesfromclient.EMove;

public class StrategySwitcher {

	final Logger logger = LoggerFactory.getLogger(StrategySwitcher.class);
	boolean hasTreasure = false;
		
	/**
	 * 
	 * Strategy pattern class for switching between treasure or castle finding strategy
	 * getMove is the method, that calls the fitting strategy and returns the next move 
	 * 
	 * @param movementContext The helper class used for storing the needed objects for the main pathfinding algorithm
	 * @param findPath The findPath instance used during the course of the whole game
	 * @return The next valid move
	 */
	public EMove getMove(IMovementContext movementContext, FindPath findPath) {

		logger.info("It is your players turn to make a move ... where will he go? 🧐");

		// Starting with the treasure finding strategy
		logger.info("Checking if I have the treasure...");
		EMove nextMove;
		
		if (!hasTreasure && movementContext.getHasTreasure()) {
			logger.info("Treasure found");
			findPath.clearCurrentPath();
			hasTreasure = true;
		}
		
		if (!hasTreasure) {

			logger.info("Finding treasure...");

			nextMove = new TreasureStrategy().pathfinding(movementContext, findPath);
		}

		// Switching to the castle finding strategy after the AI finds the treasure
		else {
			logger.info("Finding castle...");

			nextMove = new CastleStrategy().pathfinding(movementContext, findPath);
		}

		return nextMove;

	
	}

}
