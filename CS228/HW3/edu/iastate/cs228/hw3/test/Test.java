package edu.iastate.cs228.hw3.test;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author brianreber
 *
 */
public class Test {

	public static void main(String[] args) {
		
		ArrayList<String> list = new ArrayList<String>();
		
		
		ListIterator<String> iter = list.listIterator();
		System.out.println(iter.hasNext());//False
		System.out.println(iter.hasPrevious());//False
		iter.add("Hi");
		System.out.println(iter.hasNext());//False
		System.out.println(iter.hasPrevious());//True
		System.out.println(iter.previous());//Hi
	}
	
}
