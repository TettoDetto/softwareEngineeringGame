package client.cli;

import messagesbase.messagesfromclient.ETerrain;

public class TerrainToString {

	public String toString(ETerrain terrain) {
		switch (terrain) {
		case Water:
			return "[	🌊	]";
		case Grass:
			return "[	🌼	]";
		case Mountain:
			return "[	⛰️	]";
		default:
			return "[	?	]";
		}
	}

}
