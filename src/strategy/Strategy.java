package strategy;

import java.util.ArrayList;

import units.Unit;

import commander.MyCommander;

public abstract class Strategy implements StrategyInterface {

	private ArrayList<Unit> units;
	private MyCommander commander;
	private int maxNumberOfUnits = 100;
	private boolean verbose = false;
	
	public Strategy(MyCommander commander, boolean verbose) {
		units = new ArrayList<Unit>();
		this.commander = commander;
		this.verbose = verbose;
	}
	
	protected void printToLog(String msg) {
		if(verbose) {
			System.out.println(msg);
		}
	}
	
	@Override
	public boolean addUnit(Unit unit) {
		if(units.size()<maxNumberOfUnits) {
			units.add(unit);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void removeUnit(Unit unit) {
		units.remove(unit);
	}
	
	@Override
	public void removeUnit(int unitIndex) {
		units.remove(unitIndex);
	}

	public MyCommander getCommander() {
		return commander;
	}

	public ArrayList<Unit> getUnits() { return units; }

	public void setUnits(ArrayList<Unit> units) { this.units = units; }
	
	public boolean contains(Unit unit) {
		for(Unit u : units) {
			if(u.getName()==unit.getName()) {
				return true;
			}
		}
		return false;
	}

	public Unit getUnitWithName(String name) {
		for(Unit unit : units) {
			if(unit.getName()==name) {
				return unit;
			}
		}
		return null;
	}

	public int getMaxNumberOfUnits() {
		return maxNumberOfUnits;
	}

	public void setMaxNumberOfUnits(int maxNumberOfUnits, boolean trimIfTooLarge) {
		this.maxNumberOfUnits = maxNumberOfUnits;
		if(maxNumberOfUnits<units.size()&&trimIfTooLarge) {
			trimUnits();
		}
	}
	
	public void trimUnits() {
		for(Unit u : getUnits()) {
			if(getMaxNumberOfUnits()<getUnits().size()) {
				removeUnit(u);
			}
		}
	}

}
