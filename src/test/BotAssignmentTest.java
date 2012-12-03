package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.aisandbox.cmd.info.BotInfo;

import strategy.Strategy;
import units.Bot;
import units.Unit;

import commander.BotAssignment;

public class BotAssignmentTest {

	//Fake data for the bot
	private static String botInfoString = "{\"__class__\": \"BotInfo\", \"__value__\": { \"name\": \"Red3\", \"team\": \"Red\",\"position\": [35.6309928894043, 26.81215476989746],\"facingDirection\": [0.9375345706939697, -0.3478919267654419],\"flag\": \"BlueFlag\",\"currentAction\": \"ShootAtCommand\",\"state\": 6,\"health\": 0,\"seenlast\": 13.370665550231934,\"visibleEnemies\": [\"Blue0\", \"Blue1\"],\"seenBy\": [\"Blue0\", \"Blue1\"]}}";
	
	@Test
	public void test() {
		ArrayList<Strategy> strategies = new ArrayList<Strategy>();
		Strategy overflow = new testStrategy(1000, 0, "Overflow");
		strategies.add(new testStrategy(1000, 2, "Needs 2"));
		strategies.add(new testStrategy(3, 0, "Limit 3"));
		
		BotInfo b = new BotInfo(botInfoString);
		Assert.assertNotNull(BotAssignment.chooseSuitableStrategy(new Bot(null, b), strategies, overflow));
		Strategy returnedStrategy = BotAssignment.chooseSuitableStrategy(new Bot(null, b), strategies, overflow);
		Assert.assertEquals("Needs 2", ((testStrategy)returnedStrategy).getName());
		strategies.remove(returnedStrategy);
		
		returnedStrategy = BotAssignment.chooseSuitableStrategy(new Bot(null, b), strategies, overflow);
		Assert.assertEquals("Limit 3", ((testStrategy)returnedStrategy).getName());
		strategies.remove(returnedStrategy);
		
		returnedStrategy = BotAssignment.chooseSuitableStrategy(new Bot(null, b), strategies, overflow);
		Assert.assertEquals("Overflow", ((testStrategy)returnedStrategy).getName());
		strategies.remove(returnedStrategy);
	}	
	
	private class testStrategy extends Strategy {
		
		private String name = "";

		public testStrategy(int maxBots, int minBots, String name) {
			super(null, false);
			setMaxNumberOfUnits(maxBots, false);
			setMinNumberOfUnits(minBots);
			this.name = name;
		}
		
		public String getName() {
			return name;
		}

		@Override
		public void tick(Unit unit) { 
			//This strategy doesn't do anything
		}

		@Override
		public Class<?> acceptedUnitType() {
			return Unit.class;
		}
		
	}
}
