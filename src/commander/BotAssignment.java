package commander;

import java.util.ArrayList;
import java.util.List;

import strategy.Strategy;
import units.Unit;

public class BotAssignment {

	/**
	 * Adds a bot to a strategy (adds him to the overflow if needs be)
	 * @param bot the bot to add
	 * @param preferenceSet the set of strategies that need that bot
	 * @param takeSet the strategies that'll take the poor dude
	 */
	public static Strategy chooseSuitableStrategy(Unit b, List<Strategy> strategies, Strategy overflowStrategy) {
		
		ArrayList<Strategy> needsBots = new ArrayList<Strategy>();
		ArrayList<Strategy> canTakeBots = new ArrayList<Strategy>();
		
		for(Strategy s : strategies) {
			if(s.needsMoreUnits()) {
				needsBots.add(s);
			} else if(!s.isFull()) {
				canTakeBots.add(s);
			}
		}
		
		for(Strategy s : needsBots) {
			if(s.addUnit(b)) {
				return s;
			}
		}
		for(Strategy s : canTakeBots) {
			if(s.addUnit(b)) {
				return s;
			}
		}
		if(overflowStrategy.addUnit(b)) {
			return overflowStrategy;
		}
		return null;
	}
}
