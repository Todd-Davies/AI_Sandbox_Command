package analysis;

import java.util.ArrayList;

import com.aisandbox.util.Vector2;

/**
 * A class that provides many methods for analysing corners
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class CornerAnalysis {
	
	/**
	 * Finds all the corners within a map
	 * @param area the map (where 0 is a space, 1 is a small wall and 2 is a big wall)
	 * @return an ArrayList of Corners
	 */
	public static ArrayList<Corner> findCorners(int[][] area) {
		ArrayList<Corner> corners = new ArrayList<Corner>();
		for (int i = 0; i < area.length; i++) { // each row
			for (int j = 0; j < area[i].length; j++) { // each column
				if (area[i][j] == 0) { // it's not a wall - lets test to see if
										// it's a corner
					if (CornerAnalysis.isCorner(area, i, j, true)) {
						Corner c = new Corner(new Vector2(i, j), new Vector2());
						if(isDeepCorner(area, c)) { c.setDeep(true); }
						corners.add(c);
					}
				}
			}
		}
		return corners;
	}
	
	/**
	 * Computes whether the corner is deep (more than one wall eother side)
	 * @param area the map
	 * @param corner the corner to analyse
	 * @return whether the corner is deep (true) or not (false)
	 */
	private static boolean isDeepCorner(int[][] area, Corner corner) {
		Vector2 o = getCornerOrientation(area, new Corner(new Vector2((int)corner.getLocation().x, (int)corner.getLocation().y), new Vector2()));
		int x1 = (int)corner.getLocation().x + ((int)o.x*2) , x2 = (int)corner.getLocation().x, y1 = (int)corner.getLocation().y, y2 = (int)corner.getLocation().y + ((int)o.y*2);
		if(isWallFacingInDirection(area, x1, y1, o)) {
			if(isWallFacingInDirection(area, x2, y2, o)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Computes whether the specific location is a corner
	 * @param area the map
	 * @param x x coordinate on the map
	 * @param y y coordinate on the map
	 * @param countEdges should the edges of the map be taken as a wall?
	 * @return whether there's a corner at that location (true) or not (false)
	 */
	private static boolean isCorner(int[][] area, int x, int y, boolean countEdges) {
		// Find the coordinates of the spaces adjacent to this one
		int[][] around = { { x - 1, y },// Left 1
				{ x + 1, y },// Right 1
				{ x, y - 1 },// Down 1
				{ x, y + 1 } };// Up 1
		int walls = 0; // Create a variable to store how many walls are next to
						// the current space
		for (int[] coords : around) {// test each adjacent space
			if ((coords[1] < 0) || (coords[0] >= area.length)
					|| (coords[0] < 0) || (coords[1] >= area[x].length)) {
				// If this square is out of the map then it counts as a wall
				if (countEdges) {
					walls++;
				}
			} else if (area[coords[0]][coords[1]] >= 1) {
				// The space has a wall next to it
				walls++;
			}
		}
		if (walls == 2 || walls == 3) { // if the block is surrounded by 2 or 3
										// walls, it's a corner			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Is the wall facing in that direction?
	 * @param area the map
	 * @param x x coord
	 * @param y y coord
	 * @param direction the direction the wall should be facing in
	 */
	private static boolean isWallFacingInDirection(int[][] area, int x, int y, Vector2 direction) {
		if(area[x - (int)direction.x][y - (int)direction.y] > 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * Is the corner facing in that direction?
	 * @param area the map
	 * @param corner the corner to analyse
	 * @param direction the direction that the corner should be facing
	 */
	public static boolean isCornerPointingInDirectionOf(int[][] area, Corner corner, Vector2 direction) {
		Vector2 orientation = getCornerOrientation(area, corner);
		boolean x = false;
		boolean y = false;
		if (orientation.x > 0) {
			if (corner.getLocation().x < direction.x) {
				x = true;
			} else {
				x = false;
			}
		} else {
			if (corner.getLocation().x > direction.x) {
				x = true;
			} else {
				x = false;
			}
		}
		if (orientation.y > 0) {
			if (corner.getLocation().y < direction.y) {
				y = true;
			} else {
				y = false;
			}
		} else {
			if (corner.getLocation().y > direction.y) {
				y = true;
			} else {
				y = false;
			}
		}
		return (x && y);
	}

	/**
	 * Finds which direction of the map the corner is pointing in
	 * 
	 * @param area the area of the map
	 * @param corner the coordinates of the corner to test
	 * @return a Vector2 with the x and y orientation
	 */
	public static Vector2 getCornerOrientation(int[][] area, Corner corner) {
		Vector2 cornerLocation = corner.getLocation();
		if ((cornerLocation.x < 1) || (cornerLocation.x > (area.length) - 2)
				|| (cornerLocation.y < 1)
				|| (cornerLocation.y > (area[0].length) - 2)) {
			return new Vector2(0, 0);
		}
		float[][] around = {
				{
						area[(int) cornerLocation.x - 1][(int) cornerLocation.y - 1],
						area[(int) cornerLocation.x][(int) cornerLocation.y - 1],
						area[(int) cornerLocation.x + 1][(int) cornerLocation.y - 1] },
				{
						area[(int) cornerLocation.x - 1][(int) cornerLocation.y],
						area[(int) cornerLocation.x][(int) cornerLocation.y],
						area[(int) cornerLocation.x + 1][(int) cornerLocation.y] },
				{
						area[(int) cornerLocation.x - 1][(int) cornerLocation.y + 1],
						area[(int) cornerLocation.x][(int) cornerLocation.y + 1],
						area[(int) cornerLocation.x + 1][(int) cornerLocation.y + 1] } };
		Vector2 orientation = new Vector2(0, 0);
		if (around[0][1] >= 1 && around[2][1] == 0) {
			orientation.y = 1;
		} else {
			if (around[0][1] == 0 && around[2][1] >= 1) {
				orientation.y = -1;
			}
		}

		if (around[1][0] == 0 && around[1][2] >= 1) {
			orientation.x = -1;
		} else {
			if (around[1][0] >= 1 && around[1][2] == 0) {
				orientation.x = 1;
			}
		}
		return orientation;
	}

	/**
	 * Are these two vectors within a distance from eachother
	 * @param p1 vector 1
	 * @param p2 vector 2
	 * @param maxDistance the maximum distance they should be away from eachother
	 * @return
	 */
	public static boolean isWithinDistance(Vector2 p1, Vector2 p2,
			double maxDistance) {
		double distance = Math.sqrt(Math.pow(p1.x - p2.x, 2)
				+ Math.pow(p1.y - p2.y, 2));
		return (distance < maxDistance);
	}

}
