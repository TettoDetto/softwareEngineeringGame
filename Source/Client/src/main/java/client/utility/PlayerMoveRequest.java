package client.utility;

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

	public PlayerMoveRequest(String playerId, String move) {
		this.playerId = playerId;

		switch (move) {
		case "Up":
			this.move = EMove.Up;
			break;
		case "Down":
			this.move = EMove.Down;
			break;
		case "Left":
			this.move = EMove.Left;
			break;
		case "Right":
			this.move = EMove.Right;
			break;
		default:
			throw new IllegalArgumentException("Invalid move: " + move);
		}
	}
}
