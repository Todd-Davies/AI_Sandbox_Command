package strategy;

import units.Unit;

import com.aisandbox.util.Vector2;
import commander.MyCommander;

public class HunterKillerStrategy extends Strategy {
	
	public HunterKillerStrategy(MyCommander commander, boolean verbose) {
		super(commander, verbose);
	}
	
	@Override
	public void tick(Unit unit) {
		if(unit.hasFlag()) {
			Vector2 scoreLocation = getCommander().getLevelInfo().getFlagScoreLocations().get(getCommander().getGameInfo().getTeam());
			unit.move(scoreLocation, "Running to my base");
			printToLog(HunterKillerStrategy.class.getSimpleName() + " - " + unit.getName() + " has the flag and is running to base");
		} else {
			Vector2 position = getCommander().getLevelInfo().findRandomFreePositionInBox(getCommander().getLevelInfo().getLevelArea());
			unit.attack(position, "Hunting for enemy", null);
			printToLog(HunterKillerStrategy.class.getSimpleName() + " - " + unit.getName() + " is attacking position " + (int)position.x + "," + (int)position.y);
		}
	}

}