package units;

import java.util.List;

import com.aisandbox.cmd.info.BotInfo;
import com.aisandbox.util.FacingDirection;
import com.aisandbox.util.Vector2;

/**
 * Provides the interface that all classes that override the Unit class should adhere to.
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public interface UnitInterface {
	
	/**
	 * Move a unit to a location
	 * @param location the location
	 * @param message the message to display on the in-game map
	 */
	public void move(Vector2 location, String message);
	/**
	 * Move a unit to a location with waypoints
	 * @param locations the waypoints
	 * @param message the message to display on the in-game map
	 */
	void move(List<Vector2> locations, String message);
	
	/**
	 * Make a unit attack a location and face in a particular direction while doing so
	 * @param location the location
	 * @param message the message to display on the in-game map
	 * @param facing where the unit should look as they travel
	 */
	public void attack(Vector2 location, String message, Vector2 facing);
	/**
	 * Make a unit attack a location
	 * @param location the location
	 * @param message the message to display on the in-game map
	 */
	public void attack(Vector2 location, String message);
	/**
	 * Make a unit attack a set of locations
	 * @param locations the locations to visit
	 * @param message the message to display in verbose mode
	 * @param facing which direction to face
	 */
	public void attack(List<Vector2> locations, String message, Vector2 facing);
	
	/**
	 * Make a unit defend a location facing a particular way
	 * @param facing direction to face
	 * @param message the message to display on the in-game map
	 */
	public void defend(Vector2 facing, String message);
	/**
	 * Make a unit defend a location and face in a set of directions while doing so
	 * @param facing the location
	 * @param message the message to display on the in-game map
	 */
	public void defend(List<FacingDirection> facing, String message);
	/**
	 * Does this unit have the flag?
	 */
	public boolean hasFlag();
	
	/**
	 * Does this unit contain a specific bot?
	 * @param bot the BotInfo object to look for
	 * @return true if the unit contains the bot, false if it doesn't.
	 */
	public boolean contains(BotInfo bot);
	
	/**
	 * Sets all bots in the unit with this name to this BotInfo
	 * @param botName the name of the Bot
	 * @param bot the BotInfo to replace the Bot with
	 */
	public void setBot(String botName, BotInfo bot);
	
	/**
	 * Gets the current position of the unit
	 */
	public Vector2 getPosition();
}
