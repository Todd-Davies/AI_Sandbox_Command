package units;

import java.util.List;

import com.aisandbox.cmd.cmds.AttackCmd;
import com.aisandbox.cmd.cmds.DefendCmd;
import com.aisandbox.cmd.cmds.MoveCmd;
import com.aisandbox.cmd.info.BotInfo;
import com.aisandbox.util.FacingDirection;
import com.aisandbox.util.Vector2;

import commander.MyCommander;
import commander.MyCommanderV1;

/**
 * A wrapper for the BotInfo class that extends the Unit class
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class Bot extends Unit {

	private BotInfo bot;
	
	/**
	 * Bot constructor
	 * @param myCommander sets the commander that this bot belongs to
	 * @param bot the BotInfo that this class wraps around
	 */
	public Bot(final MyCommander myCommander, final BotInfo bot) {
		super(myCommander, bot.getName());
		this.setBot(bot);
	}

	@Override
	public void move(Vector2 location, String message) {
		getCommander().issue(new MoveCmd(getName(), location, message));
	}
	
	@Override
	public void move(List<Vector2> locations, String message) {
		getCommander().issue(new MoveCmd(getName(), locations, message));
	}
	
	@Override
	public void attack(Vector2 location, String message) {
		getCommander().issue(new AttackCmd(getName(), location, null, message));
	}
	
	@Override
	public void attack(Vector2 location, String message, Vector2 facing) {
		getCommander().issue(new AttackCmd(getName(), location, facing, message));
	}
	
	@Override
	public void attack(List<Vector2> locations, String message, Vector2 facing) {
		//getCommander().issue(new AttackCmd(getName(), locations, facing, message));
		getCommander().issue(new AttackCmd(getName(), locations, null, message));		
	}
	
	@Override
	public void defend(Vector2 facingDirection, String message) {
		getCommander().issue(new DefendCmd(getName(), facingDirection, message));
	}	
	
	@Override
	public void defend(List<FacingDirection> facing, String message) {
		getCommander().issue(new DefendCmd(getName(), facing, message));
	}	


	/**
	 * Gets the BotInfo that this Bot is wrapping
	 */
	public BotInfo getBot() { return bot; }

	/**
	 * Set the BotInfo for this Bot to wrap around
	 */
	public void setBot(BotInfo bot) { this.bot = bot; }

	/**
	 * Does this bot have the flag?
	 */
	@Override
	public boolean hasFlag() { return bot.hasFlag(); }

	@Override
	public boolean contains(BotInfo bot) {
		if(bot.getName().equals(this.bot.getName())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setBot(String botName, BotInfo bot) {
		setBot(bot);
	}

	@Override
	public Vector2 getPosition() {
		return getBot().getPosition();
	}

	@Override
	public int getMaxParticipants() {
		return 1;
	}
	
	@Override
	public int getMinParticipants() {
		return 1;
	}
	
}
