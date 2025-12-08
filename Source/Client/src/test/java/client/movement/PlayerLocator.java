package client.movement;

import java.awt.Point;

import client.utility.IPlayerPosition;

public class PlayerLocator implements IPlayerPosition {

	private int x;
	private int y;

	public PlayerLocator(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void update(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}

	@Override
	public Point playerLocation() {
		return new Point(x, y);
	}

}
