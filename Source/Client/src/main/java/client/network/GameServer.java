package client.network;

import client.map.placers.MapNode;
import client.network.data.PlayerMoveRequest;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
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

	public boolean sendPlayerMove(String playerId, EMove move) {
		PlayerMoveRequest moveRequest = new PlayerMoveRequest(playerId, move);
		return network.sendPlayerMove(moveRequest);
	}

	public boolean sendMapHalf(MapNode[][] mapHalf) {
		return network.sendMapHalf(mapHalf);
	}

}
