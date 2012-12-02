package strategy;

import java.util.ArrayList;

import units.Unit;

import commander.MyCommander;

/**
 * Strategy class - handles all the lower level stuff
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public abstract class Strategy implements StrategyInterface {

	private ArrayList<Unit> units;
	private MyCommander commander;
	private int maxNumberOfUnits = 100;
	private int minNumberOfUnits = 00;
	private boolean verbose = false;
	
	/**
	 * Strategy constructor
	 * @param myCommander the commander that this strategy belongs to
	 * @param verbose should the strategy tell the log what it's doing
	 */
	public Strategy(MyCommander myCommander, boolean verbose) {
		units = new ArrayList<Unit>();
		this.commander = myCommander;
		this.verbose = verbose;
	}
	
	/**
	 * If verbose is true, then print the message to the log
	 * @param msg the message to print
	 */
	protected void printToLog(String msg) {
		if(verbose) {
			System.out.println(msg);
		}
	}
	
	/**
	 * If the strategy has space, then add the unit
	 */
	@Override
	public boolean addUnit(Unit unit) {
		if(units.size()<maxNumberOfUnits) {
			return units.add(unit);
		} else {
			return false;
		}
	}

	/**
	 * Remove the unit from the strategy
	 */
	@Override
	public boolean removeUnit(Unit unit) {
		return units.remove(unit);
	}
	
	/**
	 * Removes the unit from the strategy
	 */
	@Override
	public boolean removeUnit(int unitIndex) {
		return (null!=units.remove(unitIndex));
	}

	/**
	 * Gets the commander of the strategy
	 * @return the Commander
	 */
	public MyCommander getCommander() {
		return commander;
	}

	/**
	 * Gets all units belonging to the strategy
	 * @return the units in the strategy
	 */
	public ArrayList<Unit> getUnits() { return units; }

	/**
	 * Bulk set the units in the strategy
	 * @param units the units to set
	 */
	public void setUnits(ArrayList<Unit> units) { this.units = units; }
	
	/**
	 * Does this strategy contain this unit?
	 */
	public boolean contains(Unit unit) {
		return units.contains(unit);
		/*
		for(Unit u : units) {
			if(u.getName()==unit.getName()) {
				return true;
			}
		}
		return false;*/
	}

	/**
	 * Returns the unit with a specified name
	 * @param name the name of the unit
	 * @return the unit with the name
	 */
	public Unit getUnitWithName(String name) {
		for(Unit unit : units) {
			if(unit.getName()==name) {
				return unit;
			}
		}
		return null;
	}

	/**
	 * How many units does this strategy support?
	 * @return
	 */
	public int getMaxNumberOfUnits() {
		return maxNumberOfUnits;
	}

	/**
	 * Set the maximum number of units to support
	 * @param maxNumberOfUnits the max number of units
	 * @param trimIfTooLarge should the units be auto-trimmed if they're too long
	 */
	public void setMaxNumberOfUnits(int maxNumberOfUnits, boolean trimIfTooLarge) {
		this.maxNumberOfUnits = maxNumberOfUnits;
		if(maxNumberOfUnits<units.size()&&trimIfTooLarge) {
			trimUnits();
		}
	}
	
	public int getMinNumberOfUnits() {
		return minNumberOfUnits;
	}
	
	/**
	 * Sets the minimum number of units the strategy will support
	 * @param minNumberOfUnits
	 * @throws IllegalArgumentException if the minimum > maximum
	 */
	public void setMinNumberOfUnits(int minNumberOfUnits) throws IllegalArgumentException {
		if(minNumberOfUnits>maxNumberOfUnits) throw new IllegalArgumentException("Minimum cannot be larger than maximum");
		this.minNumberOfUnits = minNumberOfUnits;
	}

	
	/**
	 * Get rid of units if there are too many
	 */
	public void trimUnits() {
		for(Unit u : getUnits()) {
			if(getMaxNumberOfUnits()<getUnits().size()) {
				removeUnit(u);
			}
		}
	}

	public boolean isFull() {
		return (getMaxNumberOfUnits()<=getUnits().size());
	}

	public boolean needsMoreUnits() {
		return (getMinNumberOfUnits()>getUnits().size());
	}

}
