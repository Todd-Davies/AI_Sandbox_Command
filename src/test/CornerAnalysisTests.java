package test;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import analysis.Corner;
import analysis.CornerAnalysis;

import com.aisandbox.util.Vector2;

public class CornerAnalysisTests {
	
	int[][] cornerTest = { {0, 0, 0, 0, 0},
						   {0, 1, 1, 0, 0},
						   {0, 0, 1, 0, 0},
						   {0, 0, 0, 0, 0},
						   {0, 0, 0, 0, 0} };
	
	int[][] deepCornerTest = { {0, 0, 0, 0, 0},
			   				   {0, 1, 0, 0, 0},
			   				   {0, 1, 0, 0, 0},
			   				   {0, 1, 1, 1, 0},
			   				   {0, 0, 0, 0, 0} };
	
	int[][] alcoveTest = { {0, 0, 0, 0, 0},
			   			   {0, 1, 1, 1, 0},
			   			   {0, 1, 0, 1, 0},
			   			   {0, 1, 0, 1, 0},
			   			   {0, 0, 0, 0, 0} };
	
	int[][] boxTest = { {0, 0, 0, 0, 0},
			            {0, 1, 1, 1, 0},
			            {0, 1, 0, 1, 0},
			            {0, 1, 1, 1, 0},
			            {0, 0, 0, 0, 0} };

	@Test
	public void testFindCorners() {
		ArrayList<Corner> corners = CornerAnalysis.findCorners(cornerTest, false);
		//Check there was something returned
		Assert.assertNotNull(corners);
		//Check there was only one Corner returned 
		Assert.assertEquals(1, corners.size());
		//Check that the right corner was returned
		Assert.assertEquals(0, new Vector2(1,2).compareTo(corners.get(0).getLocation()));
		//Check that is isn't deep
		Assert.assertFalse(corners.get(0).isDeep());
		//Is the corner direction working for -x and +y
		Assert.assertTrue(CornerAnalysis.isCornerPointingInDirectionOf(cornerTest, corners.get(0), new Vector2(0, 4)));
		
		corners = CornerAnalysis.findCorners(deepCornerTest, false);
		//Check there was something returned
		Assert.assertNotNull(corners);
		//Check there was only one Corner returned 
		Assert.assertEquals(1, corners.size());
		//Check that the corner found is deep
		Assert.assertTrue(corners.get(0).isDeep());
		//Is the corner direction working for +x and -y
		Assert.assertTrue(CornerAnalysis.isCornerPointingInDirectionOf(deepCornerTest, corners.get(0), new Vector2(4, 0)));
		
		corners = CornerAnalysis.findCorners(deepCornerTest, true);
		//Check there was something returned
		Assert.assertNotNull(corners);
		//Check there was five Corners found (coz there's walls) 
		Assert.assertEquals(5, corners.size());
		
		//Is the within distance calculator working?
		Assert.assertTrue(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(0,5), 6));
		Assert.assertFalse(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(0,5), 4));
		Assert.assertTrue(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(5,0), 6));
		Assert.assertFalse(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(5,0), 4));
		Assert.assertTrue(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(3,4), 6));
		Assert.assertFalse(CornerAnalysis.isWithinDistance(new Vector2(0,0), new Vector2(3,4), 4));
		
		corners = CornerAnalysis.findCorners(alcoveTest, false);
		//Check there was something returned
		Assert.assertNotNull(corners);
		//Check there was 1 Corner found
		Assert.assertEquals(1, corners.size());
		//Check that the alcove is still counted as a corner
		Assert.assertEquals(0, new Vector2(2,2).compareTo(corners.get(0).getLocation()));
		
		corners = CornerAnalysis.findCorners(boxTest, false);
		//Check there was something returned
		Assert.assertNotNull(corners);
		//Check there was 1 Corner found
		Assert.assertEquals(0, corners.size());
	}

}
