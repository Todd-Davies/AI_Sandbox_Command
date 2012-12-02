package commander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
		}
	}
	
	private void assignBotsToStrategies() {
		List<BotInfo> botsNotInStrategies = gameInfo.botsAvailable();
		for(BotInfo b : botsNotInStrategies) {
			Unit u = isInUnit(b);
			if(u!=null) {
				if(isInStrategy(u)==null) {
					units.remove(u);
				} else {
					//This bot is in a strategy
					botsNotInStrategies.remove(b);
				}
			} else {
				
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
