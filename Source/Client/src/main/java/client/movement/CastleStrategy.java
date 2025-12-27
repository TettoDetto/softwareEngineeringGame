package client.movement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.movement.model.IMovementContext;
import client.utility.IPlayerPosition;
import messagesbase.messagesfromclient.EMove;

public class CastleStrategy implements IPathfindingStrategy {
	
	private final static Logger logger = LoggerFactory.getLogger(CastleStrategy.class);
	
	
	/**
	 * Castle finding strategy called by the StrategySwitcher class. Uses findPath with hasTreasure set to true
	 * 
	 */
	@Override
	public EMove pathfinding(IMovementContext context, FindPath findPath) {
		
		
		logger.info("Finding the enemy castle!");
		
		IPlayerPosition playerPos = context.getPlayerPosition();
		
		return findPath.nextMove(true, playerPos);
	}

	@Override
	public String toString() {
		return String.format("Castle Strategy");
	}
}
