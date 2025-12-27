package client.main;

public class GameResult {

	private boolean hasTreasure;
	private boolean hasWon;
	private int loopCount;

	/**
	 * 
	 * Holds the result of the game, after it ended, in order to output it to the user
	 * 
	 * @param hasWon If the client has won or not
	 * @param loopCount The amount of game loops it took to achieve a game end
	 * @param hasTreasure If the client ended with the treasure or not
	 */
	public GameResult(boolean hasWon, int loopCount, boolean hasTreasure) {
		this.hasWon = hasWon;
		this.loopCount = loopCount;
		this.hasTreasure = hasTreasure;
	}

	public boolean hasWon() {
		return hasWon;
	}

	public int getRounds() {
		return loopCount;
	}

	public boolean hasTreasure() {
		return hasTreasure;
	}
}
