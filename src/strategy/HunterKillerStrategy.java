package strategy;

import units.Unit;

import com.aisandbox.util.Area;
import com.aisandbox.util.Vector2;
import commander.MyCommander;

public class HunterKillerStrategy extends Strategy {
	
	private Area area = null;
	
	public HunterKillerStrategy(MyCommander myCommander, boolean verbose) {
		super(myCommander, verbose);
	}
	
	public HunterKillerStrategy(MyCommander myCommander, boolean verbose, Area area) {
		super(myCommander, verbose);
		this.area = area;
	}
	
	@Override
	public void tick(Unit unit) {
		if(unit.hasFlag()) {
			Vector2 scoreLocation = getCommander().getLevelInfo().getFlagScoreLocations().get(getCommander().getGameInfo().getTeam());
			unit.move(scoreLocation, "Running to my base");
			printToLog(HunterKillerStrategy.class.getSimpleName() + " - " + unit.getName() + " has the flag and is running to base");
		} else {
			Vector2 position;
			if(area==null) {
				position = getCommander().getLevelInfo().findRandomFreePositionInBox(getCommander().getLevelInfo().getLevelArea());
			} else {
				position = getCommander().getLevelInfo().findRandomFreePositionInBox(area);
			}
			unit.attack(position, "Hunting for enemy");
			printToLog(HunterKillerStrategy.class.getSimpleName() + " - " + unit.getName() + " is attacking position " + (int)position.x + "," + (int)position.y);
		}
	}

	@Override
	public Class<Unit> acceptedUnitType() {
		return Unit.class;
	}

}