package commander;

import java.util.ArrayList;
import java.util.List;

import strategy.CornerDefenceStrategy;
import strategy.HunterKillerStrategy;
import strategy.Strategy;
import units.Bot;
import units.Unit;
import analysis.Corner;
import analysis.CornerAnalysis;

import com.aisandbox.cmd.SandboxCommander;
import com.aisandbox.cmd.info.BotInfo;
import com.aisandbox.cmd.info.GameInfo;
import com.aisandbox.cmd.info.LevelInfo;
import com.aisandbox.util.Area;
import com.aisandbox.util.Vector2;

public class MyCommander extends SandboxCommander {

	List<Unit> units;
	List<Strategy> strategies;
	private ArrayList<Corner> corners;
	
	/**
	 * Custom commander class construtor.
	 */
	public MyCommander() {
		name = "JavaCmd";
		units = new ArrayList<Unit>();
		strategies = new ArrayList<Strategy>();
	}
	
	
	@Override
	public void initialize() {
		verbose = true;
		
		System.out.print("Running map analysis...\n");
		corners = CornerAnalysis.findCorners(levelInfo.getBlockHeights(), false);
		System.out.print(" - " + corners.size() + " corners found\n");
		System.out.print("Finished map analysis...\n");
	}
	
	@Override
	public void tick() {
		setupStrategies();
		assignBotsToStrategies();
		for(BotInfo bot : gameInfo.botsAvailable()) {
			Unit u = isInUnit(bot);
			if(u!=null) {
				Strategy s = isInStrategy(u);
				if(s!=null) {
					s.tick(s.getUnitWithName(u.getName()));
				}
			}
		}
	}
	
	private void setupStrategies() {
		if(strategies.size()<2) {
			CornerDefenceStrategy baseDefence = new CornerDefenceStrategy(this, gameInfo.getMyFlagInfo().getPosition(), true);
			strategies.add(baseDefence);
			HunterKillerStrategy hunters = new HunterKillerStrategy(this, false);
			strategies.add(hunters);
		}
	}
	
	private void assignBotsToStrategies() {
		/*TODO: 1. find all bots not in units
		 * 		2. find all units not in strategies 
		 * 		3. find all strategies with too few units
		 * 		4. find what units these strategies want
		 * 		5. create these units from free bots
		 * 		6. add these units to the strategies
		 */
		List<BotInfo> bots = gameInfo.botsAvailable();
		for(BotInfo bot : bots) {
			Unit u = isInUnit(bot);
			if(u==null) {
				Bot b = new Bot(this, bot);
				units.add(b);
				u = isInUnit(bot);
			}
			if(isInStrategy(u) != null) {
				bots.remove(bot);
			}
		}
		for(Strategy s : strategies) {
			if(s.getClass()==CornerDefenceStrategy.class) {
				if(bots.size()>0) {
					BotInfo bot = bots.get(0);
					Unit u = isInUnit(bot);
					if(u==null) {
						Bot b = new Bot(this, bot);
						units.add(b);
						u = isInUnit(bot);
					}
					s.addUnit(isInUnit(bot));
					if(isInStrategy(u) != null) {
						bots.remove(bot);
					}
				}
			}
		}
	}
	
	
	
	private Unit isInUnit(BotInfo bot) {
		for(Unit unit : units) {
			if(unit.contains(bot)) {
				return unit;
			}
		}
		return null;
	}
	
	private int getIndexOfUnitThatContainsBot(BotInfo bot) {
		int count = 0;
		for(Unit unit : units) {
			if(unit.contains(bot)) {
				return count;
			}
			count++;
		}
		return -1;
	}
	
	private Strategy isInStrategy(Unit unit) {
		for(Strategy strategy : strategies) {
			if(strategy.contains(unit)) {
				return strategy;
			}
		}
		return null;
	}
	

	/**
	 * Called when the server sends the "shutdown" message to the commander.
	 * Use this function to teardown your bot after the game is over.
	 */
	@Override
	public void shutdown()
	{
		System.out.println("<shutdown> message received from server");
	}

	public LevelInfo getLevelInfo() {
		return levelInfo;
	}

	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public ArrayList<Corner> getCorners() {
		return corners;
	}

}
