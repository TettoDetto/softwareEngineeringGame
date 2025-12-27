package client.movement;

import client.movement.model.IMovementContext;
import messagesbase.messagesfromclient.EMove;

public interface IPathfindingStrategy {

	public EMove pathfinding(IMovementContext context, FindPath findPath);

}
