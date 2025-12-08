package client.movement;

import java.awt.Point;

public class Direction {

    /**
     * 
     * Helper class to get the direction from one point to another
     * 
     * @param from Starting point, from which to check the direction
     * @param to Target point to get the direction to
     * @return The necessary direction to make a move to reach the desired point
     */
    public static String getDirection(Point from, Point to) {
        if (to.x > from.x) return "Right";
        if (to.x < from.x) return "Left";
        if (to.y > from.y) return "Down";
        if (to.y < from.y) return "Up";
        return "Right"; 
    }
	
}
