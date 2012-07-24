package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * A Generator that generates Polyominos according to the following pattern:
 * <br />
 * ITriomino:   60%<br />
 * LTetromino:  20%<br />
 * SlashDomino: 20%
 * 
 * @author Brian Reber
 */
public class BasicGenerator implements IPolyominoGenerator {

	private Random rand;
	
	/**
	 * Creates a new BasicGenerator
	 */
	public BasicGenerator() {
		rand = new Random();
	}
	
	/* (non-Javadoc)
	 * @see edu.iastate.cs228.hw1.IPolyominoGenerator#getNext()
	 */
	@Override
	public IPolyomino getNext() {
		int random = rand.nextInt(10);
		if (random < 6) {
			return new ITriomino(new Point(5,-2), getRandomColors(3));
		} else if (random >= 6 && random < 8) {
			return new LTetromino(new Point(5,-2), getRandomColors(4));
		} else {
			return new SlashDomino(new Point(5,-1), getRandomColors(2));
		}
	}
	
	/**
	 * Gets a <code>Color[]</code> of length <code>count</code> filled with random colors from
	 * <code>AbstractBlockGame.COLORS</code>
	 * @param count
	 * The number of colors needed
	 * @return
	 * A color array with count number of colors
	 */
	private Color[] getRandomColors(int count) {
		int random = rand.nextInt(AbstractBlockGame.COLORS.length);
		Color[] c = new Color[count];
		
		for (int i = 0; i < count; i++) {
			c[i] = AbstractBlockGame.COLORS[random];
			random = rand.nextInt(AbstractBlockGame.COLORS.length);
		}
		return c;
	}

}
