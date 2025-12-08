package client.cli;

public enum ECliOutput {
	PLAYER, ENEMY, PLAYERANDENEMY, FORT, ENEMYFORT, TREASURE, HASTREASURE, PLAYERANDTREAUSRE;

	@Override
	public String toString() {
		switch (this.ordinal()) {
		case 0:
			return "[	🙂	]";
		case 1:
			return "[	😈	]";
		case 2:
			return "[	🙂 & 😈	]";
		case 3:
			return "[	🏰	]";
		case 4:
			return "[	E🏰	]";
		case 5:
			return "[	💰	]";
		case 6:
			return "[	🙂 & 💰	]";
		case 7:
			return "[	🤑	]";
		default:
			return null;
		}
	}
}
