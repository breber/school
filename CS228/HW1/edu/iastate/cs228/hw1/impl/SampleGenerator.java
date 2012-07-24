package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;

import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * Example of a generator for IPolyomino objects
 */
public class SampleGenerator implements IPolyominoGenerator {

	public IPolyomino getNext() {
	  Color c = AbstractBlockGame.COLORS[0];
	  Color[] colors = {c, c, c, c};
	  return new Sampleomino(new Point(5, -1), colors);
	}
}
