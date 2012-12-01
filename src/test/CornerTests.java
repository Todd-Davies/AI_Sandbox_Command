package test;

import junit.framework.Assert;

import org.junit.Test;

import analysis.Corner;

import com.aisandbox.util.Vector2;

public class CornerTests {

	@Test(expected = AssertionError.class)
	public void testDistance() {
		Corner tester = new Corner(new Vector2(0, 0), new Vector2(0, 0));
		Assert.assertEquals(5, tester.distanceFromLocation(new Vector2(4,3)));
	}

}
