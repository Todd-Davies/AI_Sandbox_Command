package strategy;

import units.Unit;

public interface StrategyInterface {
	public void tick(Unit unit);
	
	public boolean addUnit(Unit unit);
	public void removeUnit(Unit unit);
	public void removeUnit(int unitIndex);
	
	public void trimUnits();
	
	public boolean contains(Unit unit);
}
