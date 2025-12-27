package client.utility.events;

public enum EPropertyChangeEventType {
	ERROR, EXCEPTION,

	GAME_STATE, PLAYER_STATE,

	PLAYER_REGISTERED, GAME_STARTED, MAP_CREATION_STARTED, MAP_SENT_SUCCESSFULLY, MAP_INITIALIZED, MAP_UPDATED,
	PATHFINDING_INITIALIZED, TREASURE_SEARCH_STARTED, CASTLE_SEARCH_STARTED, MOVE_CALCULATED, WAITING_FOR_OPPONENT,
	GAME_WON, GAME_LOST, INFO_MESSAGE, GAME_ENDED, MAP_VALIDATION_ERROR, TREASURE_COLLECTED;

	@Override
	public String toString() {
		switch (this) {
		case ERROR:
			return "[ERROR]";
		case EXCEPTION:
			return "[EXCEPTION]";
		case GAME_STATE:
			return "[GAMESTATE]";
		case PLAYER_STATE:
			return "[PLAYERSTATE]";
		case PLAYER_REGISTERED:
			return "[PLAYER REGISTERED]";
		case GAME_STARTED:
			return "[🎮 GAME STARTED]";
		case MAP_CREATION_STARTED:
			return "[🔧 MAP CREATION STARTED]";
		case MAP_SENT_SUCCESSFULLY:
			return "[✅ MAP SENT SUCCESFULLY]";
		case MAP_INITIALIZED:
			return "[MAP INITIALIZED]";
		case MAP_UPDATED:
			return "[🔄 MAP UPDATED]";
		case MAP_VALIDATION_ERROR:
			return "[⚠️ MAP VALIDATION ERROR]";
		case PATHFINDING_INITIALIZED:
			return "[PATHFINDING INITIALIZED]";
		case TREASURE_SEARCH_STARTED:
			return "[TREASURE SEARCH STARTED]";
		case CASTLE_SEARCH_STARTED:
			return "[CASTLE SEARCH STARTED]";
		case MOVE_CALCULATED:
			return "[🎯 MOVE CALCULATED]";
		case WAITING_FOR_OPPONENT:
			return "[⏳ WAITING FOR OPPONENT]";
		case GAME_ENDED:
			return "[GAME ENDED]";
		case GAME_WON:
			return "[🏆 GAME WON]";
		case GAME_LOST:
			return "[💀 GAME LOST]";
		case INFO_MESSAGE:
			return "[ℹ️ INFO]";
		case TREASURE_COLLECTED:
			return "[✨ TREASURE COLLECTED]";
		default:
			return null;
		}
	}
}
