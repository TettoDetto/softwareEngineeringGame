package client.movement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.utility.IPlayerPosition;

public class TreasureStrategy implements IPathfindingStrategy {
	private final static Logger logger = LoggerFactory.getLogger(TreasureStrategy.class);

	/**
	 * 
	 * Treasure strategy while the client doesn't have the treasure
	 * 
	 * @param IMovementContext Holding every object necessary for findPath
	 * @param FindPath The current game instance of findPath
	 * @return returns the direction of the next move
	 */
	@Override
	public String pathfinding(IMovementContext context, FindPath findPath) {
		logger.info("Using the treasure strategy to find the next move...");
		
		IPlayerPosition playerPos = context.getPlayerPosition();
		
		return findPath.nextMove(false, playerPos);
	}

	@Override
	public String toString() {
		return String.format("Treasure Strategy");
	}
}
