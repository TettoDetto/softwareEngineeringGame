package client.network.data;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import messagesbase.messagesfromclient.EMove;

@XmlRootElement(name = "playerMove")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerMoveRequest {

	@XmlElement(name = "move")
	private EMove move;

	@XmlElement(name = "uniquePlayerID")
	private String playerId;

	public PlayerMoveRequest() {

	}

	public PlayerMoveRequest(String playerId, EMove move) {
		this.playerId = playerId;
		this.move = move;
	}
}
