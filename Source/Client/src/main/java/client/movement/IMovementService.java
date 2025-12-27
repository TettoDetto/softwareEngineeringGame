package client.movement;

import client.movement.model.IMovementContext;
import messagesbase.messagesfromclient.EMove;

public interface IMovementService {

	void executeMovementService(IMovementContext movementContext, FindPath findPath);
	EMove getLastMove();
	
}
