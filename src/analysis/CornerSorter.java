package analysis;

import java.util.Comparator;

import com.aisandbox.util.Vector2;

/**
 * Sorts corners by distance from a point
 * @author Todd Davies <todd434@gmail.com>
 *
 */
public class CornerSorter implements Comparator<Corner> {
	
	private Vector2 location;
	
	public CornerSorter(Vector2 location) {
		this.location = location;
	}
	
    @Override
    public int compare(Corner o1, Corner o2) {
        if(o1.distanceFromLocation(location)>(o2.distanceFromLocation(location))) {
        	return 1;
        } else if(o1.distanceFromLocation(location)<(o2.distanceFromLocation(location))) {
        	return -1;
        } else {
        	return 0;
        }
    }
}
