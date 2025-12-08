package client.utility;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import messagesbase.messagesfromclient.ETerrain;

@XmlAccessorType(XmlAccessType.FIELD)
public class MapNodeDTO {

	@XmlElement(name = "X")
	private int x;

	@XmlElement(name = "Y")
	private int y;

	@XmlElement(name = "fortPresent")
	private boolean fortPresent;

	@XmlElement(name = "terrain")
	private ETerrain terrain;

	public MapNodeDTO() {
	}

	public MapNodeDTO(int x, int y, ETerrain terrain, boolean fortPresent) {
		this.x = x;
		this.y = y;
		this.terrain = terrain;
		this.fortPresent = fortPresent;
	}
}