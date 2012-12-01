package strategy;

import units.Unit;

/**
 * Provides the interface that all classes that override the Strategy class should adhere to.
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public interface StrategyInterface {
	/**
	 * Called when it's time for the Strategy to issue orders to the units
	 * @param unit
	 */
	public void tick(Unit unit);
	
	/**
	 * Adds a unit to the strategy
	 * @param unit the unit to add
	 * @return whether the unit was added succesfully
	 */
	public boolean addUnit(Unit unit);
	/**
	 * Removes a unit
	 * @param unit the unit to remove
	 */
	public boolean removeUnit(Unit unit);
	/**
	 * Removes a unit
	 * @param unit the index of unit to remove
	 */
	public boolean removeUnit(int unitIndex);
	
	/**
	 * If the strategy has too many units, then remove enough so it has the right number
	 */
	public void trimUnits();
	
	/**
	 * Does the strategy contain this unit
	 * @param unit the unit
	 * @return
	 */
	public boolean contains(Unit unit);
}
