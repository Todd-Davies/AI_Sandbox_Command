package strategy;

import java.util.ArrayList;

import units.Unit;

import analysis.CornerAnalysis;

import com.aisandbox.util.Vector2;

import commander.MyCommander;
import commander.MyCommanderV1;

public class FlankingStrategy extends Strategy {
	
	final boolean direction;
	private ArrayList<Vector2> waypoints;
	private Vector2 endPoint;
	private Vector2 startPoint;
	
	/**
	 * Constructor for the FlankingStrategy
	 * @param commander the commander that gives the orders
	 * @param verbose should this strategy print to the log about what it's doing
	 * @param flankingDirection which direction to flank (left = false, right = true)
	 */
	/*public FlankingStrategy(MyCommander commander, boolean verbose, final boolean flankingDirection) {
		super(commander, verbose);
		direction = flankingDirection;
		setMaxNumberOfUnits(1, true);
		waypoints = new ArrayList<Vector2>();
	}*/
	
	/**
	 * Constructor for the FlankingStrategy
	 * @param commander the commander that gives the orders
	 * @param verbose should this strategy print to the log about what it's doing
	 * @param flankingDirection which direction to flank (left = false, right = true)
	 */
	public FlankingStrategy(MyCommander commander, boolean verbose, final boolean flankingDirection, Vector2 endPosition) {
		super(commander, verbose);
		direction = flankingDirection;
		setMaxNumberOfUnits(1, true);
		waypoints = new ArrayList<Vector2>();
		endPoint = endPosition;
	}
	
	/**
	 * Finds the waypoints that the bot should go to while flanking
	 */
	private void findWaypoints() {
		
		Vector2 ours = getCommander().getGameInfo().getMyFlagInfo().getPosition();
		Vector2 theirs = getCommander().getGameInfo().getEnemyFlagInfo().getPosition();;
		Vector2 middle = new Vector2((theirs.add(ours)));
		middle = new Vector2(middle.x/2, middle.y/2);

        Vector2 d = (ours.sub(theirs));
        Vector2 left = new Vector2(-d.y, d.x).normalize();
        Vector2 right = new Vector2(d.y, -d.x).normalize();
        //Vector2 front = new Vector2(d.x, d.y).normalize();
        //waypoints.add(startPoint);
		if(direction) {
			waypoints.add(right);
		} else {
			waypoints.add(left);
		}
		waypoints.add(endPoint);
		printToLog(FlankingStrategy.class.getSimpleName() + " - " + waypoints.size() + " waypoints found");
	}

	@Override
	public void tick(Unit unit) {
		if(unit.hasFlag()) {
			Vector2 scoreLocation = getCommander().getLevelInfo().getFlagScoreLocations().get(getCommander().getGameInfo().getTeam());
			unit.move(scoreLocation, "Running to my base");
			printToLog(FlankingStrategy.class.getSimpleName() + " - " + unit.getName() + " has the flag and is running to base");
			return;
		}
		if(waypoints.size()==0) { findWaypoints(); }
		Vector2 pos = unit.getPosition();
		for(int i=0;i<waypoints.size()-1;i++) {
			if(CornerAnalysis.isWithinDistance(waypoints.get(i), pos, 10)) {
				unit.attack(waypoints.get(i+1), "attacking position");
				return;
			}
		}
		unit.attack(waypoints.get(0), "attacking position");
	}

	public Vector2 getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Vector2 endPoint) {
		this.endPoint = endPoint;
	}

	public Vector2 getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Vector2 startPoint) {
		this.startPoint = startPoint;
	}

	@Override
	public Class<?> acceptedUnitType() {
		return Unit.class;
	}

}
