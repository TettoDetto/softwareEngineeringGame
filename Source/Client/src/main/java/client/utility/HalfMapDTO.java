package client.utility;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "playerHalfMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class HalfMapDTO {

	@XmlElement(name = "uniquePlayerID")
	private String playerId;

	@XmlElementWrapper(name = "playerHalfMapNodes")
	@XmlElement(name = "playerHalfMapNode")
	private List<MapNodeDTO> mapNodes;

	public HalfMapDTO() {
		this.mapNodes = new ArrayList<>();
	}

	public void setPlayerId(String playerId2) {
		this.playerId = playerId2;
	}

	public void setMapNodes(List<MapNodeDTO> mapNodes) {
		this.mapNodes = mapNodes;
	}

}
