package commander;

import java.util.ArrayList;
import java.util.Collections;
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

public class MyCommander extends SandboxCommander {

	List<Unit> units;
	List<Strategy> strategies;
	private ArrayList<Corner> corners;
	//Used to keep any excess units busy
	private HunterKillerStrategy overflowStrategy;
	
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
		overflowStrategy = new HunterKillerStrategy(this, false);
	}
	
	@Override
	public void tick() {
		setupStrategies();
		assignBotsToStrategies();
		for(BotInfo bot : gameInfo.botsAvailable()) {
			Unit u = isInUnit(bot);
			if(u!=null) {
				u.setBot(bot.getName(), bot);
				units.set(units.indexOf(u), u);
				Strategy s = isInStrategy(u);
				if(s!=null) {
					s.tick(s.getUnitWithName(u.getName()));
				}
			}
		}
	}
	
	private void setupStrategies() {
		if(strategies.size()<1) {
			CornerDefenceStrategy baseDefence = new CornerDefenceStrategy(this, gameInfo.getMyFlagInfo().getPosition(), true);
			strategies.add(baseDefence);
			HunterKillerStrategy killer = new HunterKillerStrategy(this, false);
			killer.setMaxNumberOfUnits(2, true);
			strategies.add(killer);
		}
	}
	
	/**
	 * Assign free bots to strategies
	 */
	private void assignBotsToStrategies() {
		List<BotInfo> botPool = new ArrayList<BotInfo>();
		for(BotInfo b : gameInfo.botsAvailable()) {
			Unit u = isInUnit(b);
			if(u!=null) {
				if(isInStrategy(u)==null) {
					units.remove(u);
					botPool.add(b);
				} else {
					//This bot is in a strategy
				}
			}
			botPool.add(b);
		}
		
		
		ArrayList<Strategy> needsBots = new ArrayList<Strategy>();
		ArrayList<Strategy> canTakeBots = new ArrayList<Strategy>();
		for(Strategy s : strategies) {
			if(s.needsMoreUnits()) {
				needsBots.add(s);
			} else if(!s.isFull()) {
				canTakeBots.add(s);
			}
		}
		
		for(BotInfo b : botPool) {
			addBotToStrategies(b, needsBots, canTakeBots);
		}
		
	}
	
	/**
	 * Adds a bot to a strategy (adds him to the overflow if needs be)
	 * @param bot the bot to add
	 * @param preferenceSet the set of strategies that need that bot
	 * @param takeSet the strategies that'll take the poor dude
	 */
	private void addBotToStrategies(BotInfo bot, ArrayList<Strategy> preferenceSet, ArrayList<Strategy> takeSet) {
		Bot b = new Bot(this, bot);
		for(Strategy s : preferenceSet) {
			s = strategies.get(strategies.indexOf(s));
			if(s.addUnit(b)) {
				units.add(b);
				System.out.println("Added " + bot.getName() + " to " + s.getClass().getSimpleName());
				strategies.set(strategies.indexOf(s), s);
				return;
			}
		}
		for(Strategy s : takeSet) {
			s = strategies.get(strategies.indexOf(s));
			if(s.addUnit(b)) {
				units.add(b);
				System.out.println("Added " + bot.getName() + " to " + s.getClass().getSimpleName());
				strategies.set(strategies.indexOf(s), s);
				return;
			}
		}
		if(overflowStrategy.addUnit(b)) {
			units.add(b);
			System.out.println("Added " + bot.getName() + " to overflow - " + overflowStrategy.getClass().getSimpleName());
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
		if(overflowStrategy.contains(unit)) {
			return overflowStrategy;
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
