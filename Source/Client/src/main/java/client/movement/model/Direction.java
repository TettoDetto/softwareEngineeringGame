package client.movement.model;

import java.awt.Point;

import messagesbase.messagesfromclient.EMove;

public class Direction {

    /**
     * 
     * Helper class to get the direction from one point to another
     * 
     * @param from Starting point, from which to check the direction
     * @param to Target point to get the direction to
     * @return The necessary direction to make a move to reach the desired point
     */
    public static EMove getDirection(Point from, Point to) {
        if (to.x > from.x) return EMove.Right;
        if (to.x < from.x) return EMove.Left;
        if (to.y > from.y) return EMove.Down;
        if (to.y < from.y) return EMove.Up;
        return EMove.Right; 
    }
	
}
