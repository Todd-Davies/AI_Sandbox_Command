package units;

import commander.MyCommander;

/**
 * Provides the template for interfacable in-game unit types
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public abstract class Unit implements UnitInterface {
	
	private MyCommander commander;
	private String name;
	
	/**
	 * Unit constructor
	 * @param commander the commander that the unit belongs to
	 * @param name the name of the unit
	 */
	public Unit(final MyCommander commander, final String name) {
		this.commander = commander;
		this.setName(name);
	}

	/**
	 * Gets the commander that the unit belongs to
	 */
	public MyCommander getCommander() { return commander; }

	/**
	 * Sets the commander that the unit belongs to
	 * @param commander the commander
	 */
	public void setCommander(MyCommander commander) { this.commander = commander; }

	/**
	 * Gets the name of the unit
	 */
	public String getName() { return name; }

	/**
	 * Sets the name of the unit
	 * @param name the name
	 */
	public void setName(String name) { this.name = name; }

}
