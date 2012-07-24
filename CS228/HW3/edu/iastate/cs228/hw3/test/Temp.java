package edu.iastate.cs228.hw3.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.ListIterator;

import org.junit.Test;


/**
 * @author brianreber
 *
 */
public class Temp {
	ArrayList<String> list = new ArrayList<String>();
	
	
	@Test
	public final void testbasicIteratorTest() {
		list.add("Alice");
		list.add("Bob");
		list.add("Carl");
		list.add("Dave");
		
		ListIterator<String> iter = list.listIterator();
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals(0, iter.nextIndex());
		assertEquals(-1, iter.previousIndex());
		assertEquals(iter.next(), iter.previous());
		iter.remove();
		assertEquals(3, list.size());
		assertFalse(list.contains("Alice"));
		assertFalse(iter.hasPrevious());
		assertTrue(iter.next().equals("Bob"));
		assertEquals(1, iter.nextIndex());
		iter.add("Billy");
		assertEquals(2, iter.nextIndex());
		assertEquals("Billy", iter.previous());
	}
	
	@Test
	public final void testbasicIteratorTest1() {
		list.add("Alice");
		list.add("Bob");
		list.add("Carl");
		
		ListIterator<String> iter = list.listIterator();
		assertFalse(iter.hasPrevious());
		assertTrue(iter.hasNext());
		assertEquals(0, iter.nextIndex());
		assertEquals(-1, iter.previousIndex());
		assertEquals(iter.next(), iter.previous());
		iter.remove();
//		assertEquals(2, list.sizeWithPriority(0));
//		assertEquals(3, list.size());
		assertFalse(list.contains("Alice"));
		assertFalse(iter.hasPrevious());
		assertTrue(iter.next().equals("Bob"));
		assertEquals(1, iter.nextIndex());
		iter.add("Billy");
		assertEquals(2, iter.nextIndex());
		assertEquals("Billy", iter.previous());
		iter.next();
		iter.next();
		iter.remove();
		assertTrue(iter.hasNext());
		assertEquals("Carl", iter.next());
		assertEquals("Dave", iter.next());
		iter.remove();
		assertFalse(list.contains("Dave"));
//		assertEquals(0, list.sizeWithPriority(4));
	}
}
