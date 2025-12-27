package client.movement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.model.IMovementContext;
import client.network.Network;
import client.network.data.PlayerMoveRequest;
import messagesbase.messagesfromclient.EMove;

public class MovementService implements IMovementService{

	final Logger logger = LoggerFactory.getLogger(MovementService.class);
	boolean hasTreasure = false;
	private Network network;
	private EMove nextMove;
	private String playerID;
		
	public MovementService(Network network, String playerID) {
		this.network = network;
		this.playerID = playerID; 
	}

	/**
	 * 
	 * Strategy pattern class for switching between treasure or castle finding strategy
	 * getMove is the method, that calls the fitting strategy and returns the next move 
	 * 
	 * @param movementContext The helper class used for storing the needed objects for the main pathfinding algorithm
	 * @param findPath The findPath instance used during the course of the whole game
	 * @return The next valid move
	 */
	@Override
	public void calculateAndSendMove(IMovementContext movementContext, FindPath findPath) {

		logger.info("It is your players turn to make a move ... where will he go? 🧐");

		// Starting with the treasure finding strategy
		
		if (!hasTreasure && movementContext.getHasTreasure()) {
			logger.info("Treasure found");
			findPath.clearCurrentPath();
			hasTreasure = true;
		}
		
		if (!hasTreasure) {

			logger.info("Finding treasure...");

			this.nextMove = new TreasureStrategy().pathfinding(movementContext, findPath);
		}

		// Switching to the castle finding strategy after the AI finds the treasure
		else {
			logger.info("Finding castle...");

			this.nextMove = new CastleStrategy().pathfinding(movementContext, findPath);
		}
		sendMove();
	}
	
	private  void sendMove() {
		if (this.nextMove != null) {
			logger.info("Moving towards: " + nextMove);
			
			network.sendPlayerMove(new PlayerMoveRequest(playerID, nextMove));
		}
	}
	@Override
	public EMove getLastMove() {
		return nextMove;
	}

}
