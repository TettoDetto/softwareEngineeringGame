package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.CastleStrategy;
import client.movement.FindPath;
import client.movement.IMovementContext;
import client.movement.TreasureStrategy;

public class StrategySwitcher {

	final Logger logger = LoggerFactory.getLogger(StrategySwitcher.class);
	
		
	/**
	 * 
	 * Strategy pattern class for switching between treasure or castle finding strategy
	 * getMove is the method, that calls the fitting strategy and returns the next move 
	 * 
	 * @param movementContext The helper class used for storing the needed objects for the main pathfinding algorithm
	 * @param findPath The findPath instance used during the course of the whole game
	 * @return The next valid move
	 */
	public String getMove(IMovementContext movementContext, FindPath findPath) {

		logger.info("It is your players turn to make a move ... where will he go? 🧐");

		// Starting with the treasure finding strategy
		logger.info("Checking if I have the treasure...");
		String nextMove;


		if (!(movementContext.getHasTreasure())) {
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
