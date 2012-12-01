package analysis;

import com.aisandbox.util.Vector2;

/**
 * A class to hold and provide information about corners the map
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class Corner {
	private Vector2 location;
	private Vector2 facing;
	private boolean deep = false;
	
	/**
	 * Corner constructor
	 */
	public Corner() {
		location = new Vector2();
		facing = new Vector2();
	}
	
	/**
	 * Corner constructor
	 * @param location the location of the corner
	 * @param facing the gradient of the direction in which the corner is facing
	 */
	public Corner(Vector2 location, Vector2 facing) {
		this.location = location;
		this.facing = facing;
	}
	
	/**
	 * Computes the distance of the corner from a given location 
	 * @param flagLocation
	 * @return
	 */
	public double distanceFromLocation(Vector2 flagLocation) {
		double distance = Math.sqrt(Math.pow(location.x - flagLocation.x, 2) + 
				   Math.pow(location.y - flagLocation.y, 2));
		return distance;
	}

	/**
	 * Gets the location of the corner
	 */
	public Vector2 getLocation() {
		return location;
	}

	
	/**
	 * Sets the location of the corner
	 */
	public void setLocation(Vector2 location) {
		this.location = location;
	}

	/**
	 * Gets the location in which the corner is facing
	 */
	public Vector2 getFacing() {
		return facing;
	}

	/**
	 * Sets the location in which the location is facing
	 */
	public void setFacing(Vector2 facing) {
		this.facing = facing;
	}
	
	/**
	 * Outputs the corner info as a nice string
	 */
	@Override
	public String toString() {
		return location.x + "," + location.y + " - " + facing.x + "," + facing.y;
	}

	/**
	 * Is the corner deep, or is it just a little edge?
	 * @return
	 */
	public boolean isDeep() {
		return deep;
	}

	/**
	 * Set whether the corner is deep
	 */
	public void setDeep(boolean deep) {
		this.deep = deep;
	}
}
