package test;

import java.util.ArrayList;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

import analysis.Corner;
import analysis.CornerSorter;

import com.aisandbox.util.Vector2;

public class CornerSorterTest {

	@Test
	public void test() {
		ArrayList<Corner> corners = new ArrayList<Corner>();
		corners.add(new Corner(new Vector2(0,0), new Vector2(0,0)));
		corners.add(new Corner(new Vector2(2,0), new Vector2(0,0)));
		Collections.sort(corners, new CornerSorter(new Vector2(0,0)));
		//Check that the 0,0 corner is put first
		Assert.assertTrue(corners.get(0).getLocation().x==0);
		Collections.sort(corners, new CornerSorter(new Vector2(3,0)));
		//Check that the 2,0 corner is put first
		Assert.assertTrue(corners.get(0).getLocation().x==2);
	}

}
