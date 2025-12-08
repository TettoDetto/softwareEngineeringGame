package client.network;

import client.map.MapHalfGenerator;
import client.utility.PlayerMoveRequest;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;

public interface INetwork {

	public UniquePlayerIdentifier registerPlayer(String firstName, String lastName, String uAccount);

	public GameState getGameState() throws Exception;

	public boolean sendPlayerMove(PlayerMoveRequest move);

	public boolean sendMapHalf(MapHalfGenerator mapHalf);

}
