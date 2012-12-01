package commander;

import java.util.ArrayList;
import java.util.List;

import strategy.CornerDefenceStrategy;
import strategy.HunterKillerStrategy;
import strategy.Strategy;
import units.Bot;
import units.Squad;
import units.Unit;
import analysis.Corner;
import analysis.CornerAnalysis;

import com.aisandbox.cmd.SandboxCommander;
import com.aisandbox.cmd.info.BotInfo;
import com.aisandbox.cmd.info.GameInfo;
import com.aisandbox.cmd.info.LevelInfo;


/**
 * Sample "balanced" commander for the AI sandbox.
 * One bot attacking, one defending and the rest randomly searching the level for enemies.
 * 
 * @author Matthias F. Brandstetter
 */
public class MyCommander extends SandboxCommander
{
	
	List<Unit> units;
	List<Strategy> strategies;
	private ArrayList<Corner> corners;
	//private ArrayList<Corner> suitableCorners;
	
	boolean b = false;
	
	/**
	 * Custom commander class construtor.
	 */
	public MyCommander()
	{
		name = "JavaCmd";
		units = new ArrayList<Unit>();
		strategies = new ArrayList<Strategy>();
	}
	
	/**
	 * Called when the server sends the "initialize" message to the commander.
	 * Use this function to setup your bot before the game starts.
	 * You can also set this.verbose = true to get more information about each bot visually.
	 */
	@Override
	public void initialize()
	{
		verbose = true;
		
		System.out.print("Starting map analysis...\n");
		System.out.print("DEFENCE ANALYSIS\n");
		System.out.print(" - Finding corners to defend from...\n");
		corners = CornerAnalysis.findCorners(levelInfo.getBlockHeights(), false);
		System.out.print(" - " + corners.size() + " corners found\n");
		System.out.print("Finished map analysis...\n");
	}
	
	/**
	 * Called when the server sends a "tick" message to the commander.
	 * Override this function for your own bots.  Here you can access all the information in this.gameInfo,
	 * which includes game information, and this.levelInfo which includes information about the level.
	 * You can send commands to your bots using the issue() method in this class.
	 */
	@Override
	public void tick()
	{
		organiseBotsIntoUnits();
		organiseUnitsIntoStrategies();
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
	
	private void organiseUnitsIntoStrategies() {
		HunterKillerStrategy hunterKiller = new HunterKillerStrategy(this, true);
		CornerDefenceStrategy flagDefence = new CornerDefenceStrategy(this, gameInfo.getMyFlagInfo().getPosition(), true, 2);
		for(Unit unit : units) {
			if(isInStrategy(unit)==null) {
				if(!flagDefence.addUnit(unit)) { //Try to add the unit to flag defence
					hunterKiller.addUnit(unit); // Otherwise, have it hunt for enemies
					System.out.println("Added " + unit.getName() + "to hunterKiller");
				} else {
					System.out.println("Added " + unit.getName() + "to cornerDefence");
				}
			}
		}
		if(hunterKiller.getUnits().size()>0) {
			strategies.add(hunterKiller);
		}
		if(flagDefence.getUnits().size()>0) {
			strategies.add(flagDefence);
		}
	}
	
	private void organiseBotsIntoUnits() {
		int numberOfBots = gameInfo.getMyTeamInfo().getMembers().size();
		Squad s = new Squad(this, "Squad" + units.size(), numberOfBots-1);
		for(BotInfo bot : gameInfo.botsAvailable()) {
			Bot b = new Bot(this, bot);
			Unit u = isInUnit(bot);
			if(u==null) {
				if(!s.add(b)) {
					int uIndex = getIndexOfUnitThatContainsBot(bot);
					//u.setBot(bot.getName(), bot);
					if(uIndex<0) {
						units.add(new Bot(this, bot));
					} else {
						Unit unit = units.get(uIndex);
						unit.setBot(bot.getName(), bot);
						units.set(uIndex, unit);
					}
				} else {
					System.out.println(bot.getName() + "added to squad");
				}
			} else {
				int uIndex = getIndexOfUnitThatContainsBot(bot);
				if(u.getClass()==Bot.class) {
					u.setBot(bot.getName(), bot);
					units.set(uIndex, u);
				} else if(u.getClass()==Squad.class) {
					Squad sq = (Squad) u;
					sq.setBot(bot.getName(), bot);
					units.set(uIndex, sq);
				}
			}
			/*int u = getIndexOfUnitThatContainsBot(bot);
			if(u<0) {
				units.add(new Bot(this, bot));
			} else {
				Unit unit = units.get(u);
				unit.setBot(bot.getName(), bot);
				units.set(u, unit);
			}*/
		}
		if(s.getBots().size()>0) {
			units.add(s);
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