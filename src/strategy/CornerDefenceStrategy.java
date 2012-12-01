package strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import units.Bot;
import units.Unit;
import analysis.Corner;
import analysis.CornerAnalysis;
import analysis.CornerSorter;

import com.aisandbox.util.Vector2;
import commander.MyCommander;

/**
 * A strategy that defends a location of the map by placing bots in corners around that point to defend it
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class CornerDefenceStrategy extends Strategy {

	private Vector2 defendingPosition;
	private ArrayList<Corner> suitableCorners;
	private HashMap<String, Corner> occupiedCorners;
	
	/**
	 * CornerDefenceStrategy constructor
	 * @param commander the commmander that this strategy belongs to
	 * @param location the location to defend
	 * @param verbose whether the strategy should print what it's doing to the log
	 */
	public CornerDefenceStrategy(MyCommander commander, Vector2 location, boolean verbose) {
		super(commander, verbose);
		occupiedCorners = new HashMap<String, Corner>();
		setDefenceLocation(location);
	}
	
	/**
	 * CornerDefenceStrategy constructor
	 * @param commander the commmander that this strategy belongs to
	 * @param location the location to defend
	 * @param verbose whether the strategy should print what it's doing to the log
	 * @param numberOfUnits the maximum number of units the strategy should accept (may change)
	 */
	public CornerDefenceStrategy(MyCommander commander, Vector2 location, boolean verbose, int numberOfUnits) {
		super(commander, verbose);
		occupiedCorners = new HashMap<String, Corner>();
		setMaxNumberOfUnits(numberOfUnits, true);
		setDefenceLocation(location);
	}
	
	/**
	 * Sets the location the strategy should position bots around
	 * @param location the location to defend
	 */
	public void setDefenceLocation(Vector2 location) {
		defendingPosition = location;
		//printToLog(CornerDefenceStrategy.class.getSimpleName() + " - Choosing best corners to defend from...\n");
		suitableCorners = new ArrayList<Corner>();
		for (Corner p : getCommander().getCorners()) {
			if (CornerAnalysis.isWithinDistance(p.getLocation(), location,
					getCommander().getLevelInfo().getFiringDistance())) {
				if (CornerAnalysis.isCornerPointingInDirectionOf(getCommander().getLevelInfo().getBlockHeights(),
						p, location)) {
					if(p.isDeep()) {
						suitableCorners.add(new Corner(p.getLocation(), CornerAnalysis.getCornerOrientation(getCommander().getLevelInfo().getBlockHeights(), p)));
					}
				}
			}
		}
		Collections.sort(suitableCorners, new CornerSorter(location));
		//printToLog(CornerDefenceStrategy.class.getSimpleName() + " - " + suitableCorners.size() + " suitable corners found:\n");
		for (int i = 0; i < suitableCorners.size(); i++) {
			//printToLog("  - " + suitableCorners.get(i).toString() + "\n");
		}
		setMaxNumberOfUnits(suitableCorners.size(), true);
		//printToLog(CornerDefenceStrategy.class.getSimpleName() + " - will defend at " + suitableCorners.size() + " points\n");
	}
	
	/**
	 * Gets the corner a bot to defend from
	 * @param bot the bot to find the corner for
	 * @return null if no corner could be found, otherwise the Corner the bot should hide in
	 */
	private Corner getBotCornerPosition(Bot bot) {
		Corner pos = occupiedCorners.get(bot.getName());
		if(pos == null) {
			pos = findCornerForBot(bot);
		}
		return pos;
	}

	/**
	 * Finds a suitable corner for the bot
	 * @param bot the bot to hide
	 * @return a corner for the bot (null if there was none)
	 */
	private Corner findCornerForBot(Bot bot) {
		for (Corner c : suitableCorners) {
			if (!occupiedCorners.containsValue(c)) {
				occupiedCorners.put(bot.getName(), c);
				return c;
			}
		}
		removeUnit(bot);
		return null;
	}

	@Override
	public void tick(Unit unit) {
		if(unit.getClass()!=Bot.class) {
			return;
		}
		
		Bot bot = (Bot)unit;
		Corner pos = null;
		pos = getBotCornerPosition(bot);
		if (pos != null) {
			Vector2 goal = new Vector2(pos.getLocation().x, pos.getLocation().y);
			if (new Vector2(goal).sub(bot.getBot().getPosition()).length() > 2f) {
				bot.move(getCommander().getLevelInfo().findNearestFreePosition(goal), "Running to defence position");
				printToLog(CornerDefenceStrategy.class.getSimpleName() + " - " + bot.getName() + " is moving to defend a corner");
			} else {
				bot.defend(new Vector2(defendingPosition).sub(bot.getBot().getPosition()), "Defending position");
				printToLog(CornerDefenceStrategy.class.getSimpleName() + " - " + bot.getName() + " is defending a corner");
			}
		}
	}
	
	/**
	 * Only accepts the Bot class, otherwise it returns false.
	 */
	@Override
	public boolean addUnit(Unit unit) {
		if(unit!=null) {
			if(unit.getClass()==Bot.class) {
				return super.addUnit(unit);
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Trims units until the desired level in the following order:<ul>
	 * <li>Units that have no corner (removed first)</li>
	 * <li>Units that are in shallow corners (removed second)</li>
	 * <li>Other units (removed as a last resort)</li>
	 */
	@Override
	public void trimUnits() {
		//First trim units that don't have a corner
		if(getMaxNumberOfUnits()>=getUnits().size()) {
			return;
		}
		for(Unit u : getUnits()) {
			if(getMaxNumberOfUnits()<getUnits().size()) {
				if(!occupiedCorners.containsKey(u.getName())) {
					removeUnit(u);
				}
			}
		}
		//Then start trimming units that are in shallow corners
		if(getMaxNumberOfUnits()>=getUnits().size()) {
			return;
		}
		for(Unit u : getUnits()) {
			if(getMaxNumberOfUnits()<getUnits().size()) {
				if(!occupiedCorners.get(u.getName()).isDeep()) {
					removeUnit(u);
				}
			}
		}
		//It we still have too many, just trim any :(
		if(getMaxNumberOfUnits()>=getUnits().size()) {
			return;
		}
		for(Unit u : getUnits()) {
			if(getMaxNumberOfUnits()<getUnits().size()) {
				if(!occupiedCorners.get(u.getName()).isDeep()) {
					removeUnit(u);
				}
			}
		}
	}

}
