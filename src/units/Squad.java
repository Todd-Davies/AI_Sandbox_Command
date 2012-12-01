package units;

import java.util.ArrayList;
import java.util.List;

import com.aisandbox.cmd.info.BotInfo;
import com.aisandbox.util.FacingDirection;
import com.aisandbox.util.Vector2;
import commander.MyCommander;

/**
 * A class that organises groups of bots to perform commands together in a team
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class Squad extends Unit {
	
	private ArrayList<Bot> bots;
	private int limit = 5;

	/**
	 * Squad constructor
	 * @param commander the commander that the squad belongs to
	 * @param name the name of the squad e.g. 'sqd 1' or 'Delta team'
	 */
	public Squad(MyCommander commander, String name) {
		super(commander, name);
		bots = new ArrayList<Bot>();
	}
	
	/**
	 * Squad constructor
	 * @param commander the commander that the squad belongs to
	 * @param name the name of the squad e.g. 'sqd 1' or 'Delta team'
	 * @param limit the upper limit for the number of bots in the squad 
	 */
	public Squad(MyCommander commander, String name, int limit) {
		super(commander, name);
		bots = new ArrayList<Bot>();
		this.limit = limit;
	}

	@Override
	public void move(Vector2 location, String message) {
		for(Bot bot : bots) {
			bot.move(location, message);
		}
	}
	
	@Override
	public void move(List<Vector2> locations, String message) {
		for(Bot bot : bots) {
			bot.move(locations, message);
		}
	}
	
	@Override
	public void attack(Vector2 location, String message, Vector2 facing) {
		/*if(facing==null) {
			FlagInfo f = getCommander().getGameInfo().getMyFlagInfo();
			facing = getCommander().getGameInfo().getFlags().get(f.getName()).getPosition();
		}*/
		for(Bot bot : bots) {
			bot.attack(location, message, facing);
		}
	}
	
	/**
	 * Makes all the bots in the squad attack a location
	 */
	@Override
	public void attack(Vector2 location, String message) {
		this.attack(location, message, null);
	}
	
	/**
	 * Makes all the bots in the squad defend a location
	 */
	@Override
	public void defend(Vector2 location, String message) {
		for(Bot bot : bots) {
			bot.defend(location, message);
		}
	}
	
	/**
	 * Makes all the bots in the squad defend a location
	 */
	@Override
	public void defend(List<FacingDirection> facing, String message) {
		for(Bot bot : bots) {
			bot.defend(facing, message);
		}
	}
	
	/**
	 * Adds this bot to the squad
	 * @param bot the bot to add
	 * @return whether the bot was added sucessfully
	 */
	public boolean add(Bot bot) { 
		if(bots.size()<limit) {
			return bots.add(bot);
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the bot from the squad
	 * @param bot the index of the bot to remove
	 * @return whether the bot was successfully removed
	 */
	public boolean remove(int i) { return bots.remove(i) != null; }
	
	/**
	 * Removes the bot from the squad
	 * @param bot the bot to remove
	 * @return whether the bot was successfully removed
	 */
	public boolean remove(Bot bot) { return bots.remove(bot); }

	@Override
	public boolean hasFlag() {
		for(Bot b : bots) {
			if(b.hasFlag()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean contains(BotInfo bot) {
		for(Bot b : bots) {
			if(b.contains(bot)) {
				return true;
			}
		}
		return false;
	}
	
	public void setBot(String name, BotInfo bot) {
		for(int i=0;i<bots.size();i++) {
			Bot b = bots.get(i);
			if(b.getBot().getName().equals(name)) {
				b.setBot(bot);
				bots.set(i, b);
			}
		}
	}

	/**
	 * Gets all the bots in the squad
	 */
	public ArrayList<Bot> getBots() {
		return bots;
	}

	/**
	 * Sets the maximum amount of bots allowed in this squad
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) { this.limit = limit; }
	
	/**
	 * Gets the maximum amount of bots allowed in this squad
	 */
	public int getLimit() { return limit; }

	/**
	 * Gets the average position of all the bots in the squad
	 */
	@Override
	public Vector2 getPosition() {
		Vector2 averagePosition = new Vector2();
		for(Bot b : bots) {
			averagePosition = new Vector2((averagePosition.x + b.getPosition().x) / 2, (averagePosition.y + b.getPosition().y) / 2);
		}
		return averagePosition;
	}

}
