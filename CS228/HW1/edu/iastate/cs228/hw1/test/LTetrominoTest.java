package edu.iastate.cs228.hw1.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import org.junit.Test;

import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.impl.AbstractBlockGame;
import edu.iastate.cs228.hw1.impl.LTetromino;

/**
 * A test suite for the LTetromino type.
 * 
 * @author Brian Reber
 */
public class LTetrominoTest {

	/**
	 * Gets a <code>Color[]</code> of length <code>count</code> filled with random colors from
	 * <code>AbstractBlockGame.COLORS</code>
	 * @param count
	 * The number of colors needed
	 * @return
	 * A color array with count number of colors
	 */
	private Color[] getRandomColors(int count) {
		Random rand = new Random();
		int random = rand.nextInt(AbstractBlockGame.COLORS.length);
		Color[] c = new Color[count];
		
		for (int i = 0; i < count; i++) {
			c[i] = AbstractBlockGame.COLORS[random];
			random = rand.nextInt(AbstractBlockGame.COLORS.length);
		}
		return c;
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.LTetromino#LTetromino(java.awt.Point, java.awt.Color[])}.
	 */
	@Test
	public final void testLTetromino() {
		LTetromino ltetromino = new LTetromino(new Point(0, 0), getRandomColors(4));
		assertTrue("LTetromino should instantiate with the correct parameters", ltetromino.getCells().length == 4);
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.LTetromino#LTetromino(java.awt.Point, java.awt.Color[])}.
	 */
	@Test()
	public final void testLTetrominoWithTooFewColorsInColorArray() {
		LTetromino ltetromino = null;
		try {
			ltetromino = new LTetromino(new Point(0, 0), getRandomColors(3));
		} catch (ArrayIndexOutOfBoundsException e) {
			assertNull("LTetromino should not instantiate with too few colors in the color array", ltetromino);
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.LTetromino#LTetromino(java.awt.Point, java.awt.Color[])}.
	 */
	@Test
	public final void testLTetrominoWithTooManyColorsInColorArray() {
		LTetromino ltetromino = new LTetromino(new Point(0, 0), getRandomColors(5));
		assertNotNull("LTetromino instantiates correctly with too many colors in the color array", ltetromino);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#getCells()}.
	 */
	@Test
	public final void testGetCells() {
		LTetromino ltetromino = new LTetromino(new Point(0, 0), getRandomColors(4));
		assertTrue("LTetromino has correct amount of cells", ltetromino.getCells().length == 4);
		
		Cell[] cells = ltetromino.getCells();
		
		//Since we hardcoded the starting position, and we know how the LTetromino should place cells, we
		//will check using hardcoded values
		assertTrue("First cell should be at 0,0", cells[0].getPosition().x == 0 && cells[0].getPosition().y == 0);
		assertTrue("Second cell should be at 1,0", cells[1].getPosition().x == 1 && cells[1].getPosition().y == 0);
		assertTrue("Third cell should be at 1,1", cells[2].getPosition().x == 1 && cells[2].getPosition().y == 1);
		assertTrue("Fourth cell should be at 1,2", cells[3].getPosition().x == 1 && cells[3].getPosition().y == 2);
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#shiftDown()}.
	 */
	@Test
	public final void testShiftDown() {
		LTetromino ltetromino = new LTetromino(new Point(0, 0), getRandomColors(4));
		Point[] initialPoints = new Point[ltetromino.getCells().length];
		Cell[] cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			initialPoints[i] = cells[i].getPosition();
		}
		
		ltetromino.shiftDown();
		cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			assertTrue("Y Positions match up to being moved down", initialPoints[i].y == cells[i].getPosition().y - 1);
			assertTrue("X Positions haven't changed", initialPoints[i].x == cells[i].getPosition().x);
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#shiftLeft()}.
	 */
	@Test
	public final void testShiftLeft() {
		LTetromino ltetromino = new LTetromino(new Point(5, 0), getRandomColors(4));
		Point[] initialPoints = new Point[ltetromino.getCells().length];
		Cell[] cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			initialPoints[i] = cells[i].getPosition();
		}
		
		ltetromino.shiftLeft();
		cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			assertTrue("Y Positions haven't changed", initialPoints[i].y == cells[i].getPosition().y);
			assertTrue("X Positions have been shifted left by 1", initialPoints[i].x == cells[i].getPosition().x + 1);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#shiftRight()}.
	 */
	@Test
	public final void testShiftRight() {
		LTetromino ltetromino = new LTetromino(new Point(5, 0), getRandomColors(4));
		Point[] initialPoints = new Point[ltetromino.getCells().length];
		Cell[] cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			initialPoints[i] = cells[i].getPosition();
		}
		
		ltetromino.shiftRight();
		cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			assertTrue("Y Positions haven't changed", initialPoints[i].y == cells[i].getPosition().y);
			assertTrue("X Positions have been shifted right by 1", initialPoints[i].x == cells[i].getPosition().x - 1);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#transform()}.
	 */
	@Test
	public final void testTransform() {
		LTetromino ltetromino = new LTetromino(new Point(0, 0), getRandomColors(4));
		Point[] initialPoints = new Point[ltetromino.getCells().length];
		Cell[] cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			initialPoints[i] = cells[i].getPosition();
		}
		
		//Flip the LTetromino
		ltetromino.transform();
		cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			assertTrue("Y Positions shouldn't have changed", initialPoints[i].y == cells[i].getPosition().y);
			assertTrue("X Positions should have been flipped accross the center axis of the bounding square.",
					cells[i].getPosition().x == 3 - (initialPoints[i].x + 1));
		}
		
		//Flip it back and make sure they match the original shape
		ltetromino.transform();
		cells = ltetromino.getCells();
		
		for (int i = 0; i < initialPoints.length; i++) {
			assertTrue("Y Positions shouldn't have changed", initialPoints[i].y == cells[i].getPosition().y);
			assertTrue("X Positions should have been flipped accross the center axis of the bounding square.",
					cells[i].getPosition().x == initialPoints[i].x);
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#cycle()}.
	 */
	@Test
	public final void testCycle() {
		LTetromino ltetromino = new LTetromino(new Point(5, 0), getRandomColors(4));
		Cell[] originalCells = ltetromino.getCells();
		
		ltetromino.cycle();
		Cell[] newCells = ltetromino.getCells();
		
		for (int i = 1; i < originalCells.length; i++) {
			if (i == originalCells.length - 1) {
				assertTrue("Rotated cell colors should match the original cell colors after rotation", 
						originalCells[i].getIcon().getColorHint().equals(newCells[0].getIcon().getColorHint()));	
			} else {
				assertTrue("Rotated cell colors should match the original cell colors after rotation",
						originalCells[i - 1].getIcon().getColorHint() == newCells[i].getIcon().getColorHint());	
			}
		}
	}

	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#clone()}.
	 */
	@Test
	public final void testClone() {
		LTetromino ltetromino = new LTetromino(new Point(5, 0), getRandomColors(4));
		LTetromino cloned = (LTetromino) ltetromino.clone();
		
		assertTrue("Cloned cells array size should match original cells array size", 
				cloned.getCells().length == ltetromino.getCells().length);	
		
		Cell[] originalCells = ltetromino.getCells();
		Cell[] clonedCells = cloned.getCells(); 
		
		for (int i = 0; i < originalCells.length; i++)
		{
			assertTrue("Cloned cell icons should match the original cell icons", 
					clonedCells[i].getIcon().matches(originalCells[i].getIcon()));
			assertTrue("Cloned cell positions should match the original cell positions", 
					clonedCells[i].getPosition().equals(originalCells[i].getPosition()));
		}
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#equals(java.lang.Object o)}.
	 */
	@Test
	public final void testEqualsWhenObjectsAreEqual() {
		Color[] colors = { Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW };
		LTetromino ltetromino = new LTetromino(new Point(5, 0), colors);
		LTetromino other = new LTetromino(new Point(5, 0), colors);
		
		assertTrue("The two objects should be equal", other.equals(ltetromino));	
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#equals(java.lang.Object o)}.
	 */
	@Test
	public final void testEqualsWhenObjectsHaveDifferentIcons() {
		Color[] colors = { Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW };
		Color[] colorsOther = { Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED };
		LTetromino ltetromino = new LTetromino(new Point(5, 0), colors);
		LTetromino other = new LTetromino(new Point(5, 0), colorsOther);
		
		assertTrue("The two objects should not be equal because they have different colors", !other.equals(ltetromino));	
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#equals(java.lang.Object o)}.
	 */
	@Test
	public final void testEqualsWhenObjectsHaveDifferentPositions() {
		Color[] colors = { Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW };
		LTetromino ltetromino = new LTetromino(new Point(5, 0), colors);
		LTetromino other = new LTetromino(new Point(0, 0), colors);
		
		assertTrue("The two objects should not be equal because their positions do not match", !other.equals(ltetromino));	
	}
	
	/**
	 * Test method for {@link edu.iastate.cs228.hw1.impl.Polyomino#equals(java.lang.Object o)}.
	 */
	@Test
	public final void testEqualsWhenComparingTwoObjectsOfDifferentType() {
		Color[] colors = { Color.CYAN, Color.BLUE, Color.ORANGE, Color.YELLOW };
		LTetromino ltetromino = new LTetromino(new Point(5, 0), colors);
		
		assertTrue("The two objects should not be equal because they are of different types", !ltetromino.equals(colors));	
	}
}
