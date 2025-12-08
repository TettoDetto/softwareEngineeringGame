package client.network;

import client.map.MapHalfGenerator;
import client.utility.PlayerMoveRequest;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;

public class GameServer {

	private final INetwork network;

	public GameServer(INetwork network) {
		this.network = network;
	}

	public UniquePlayerIdentifier registerPlayer(String name, String surname, String uaccount) {
		return network.registerPlayer(name, surname, uaccount);
	}

	public GameState getGameState() {
		try {
			return network.getGameState();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean sendPlayerMove(String playerId, String move) {
		PlayerMoveRequest moveRequest = new PlayerMoveRequest(playerId, move);
		return network.sendPlayerMove(moveRequest);
	}

	public boolean sendMapHalf(MapHalfGenerator mapHalf) {
		return network.sendMapHalf(mapHalf);
	}

}
