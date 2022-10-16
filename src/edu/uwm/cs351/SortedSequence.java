// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

/*
 * Andrew Le
 * Homework 6, CS 351
 */

import java.util.Comparator;
import java.util.function.Consumer;

import edu.uwm.cs.junit.LockedTestCase;

/******************************************************************************
 * This class is a homework assignment;
 * An SortedSequence is a sequence of values in sorted order.
 * It can have a special "current element," which is specified and 
 * accessed through four methods that are available in the this class 
 * (start, getCurrent, advance and isCurrent).
 ******************************************************************************/
public class SortedSequence<E> implements Cloneable {

	// TODO: Declare the private static Node<T> class.
	// This will serve for our doubly-linked list. 
	// It should have two constructors but no methods.
	// 1. You should define a constructor with no parameters that
	//    sets up the node as a dummy node: setting prev/next/data
	//    as required.  You will need to use an "unchecked" cast, and should
	//    suppress the warning that it requires.
	// 2. You should define a constructor taking a valid data object
	//    which initializes the data and (by default) nulls the pointer fields.
	// 3. You may declare another constructor with two or more parameters.
	//    (Our solution does not use any other constructors.)
	// The fields of Node should have "default" access (neither public, nor private)
	// and should not start with underscores.
	
	private static class Node<T>{
		T data;
		Node<T> next;
		Node<T> prev;
		
		
		public Node() {
			data = null;
			next = prev = null;
		}
		public Node(T object) { 
			data = object;
			next = prev = null;
		}
	}

	
	// TODO: Declare the private fields needed for our data structure:
	// a `manyItems` field, a dummy, a cursor, and a comparator.
	// NB: You must use generics correctly: no raw types!
	
	int manyItems;
	Node<E> dummy;
	Node<E> cursor;
	Comparator<E> comparator;
	

	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	private boolean wellFormed() {
		// Check the invariant.
		// Invariant:
		// 1. The comparator may not be null
		// 2. The dummy may not be null
		// 3. The dummy's data must refer to itself.
		//    (This is an impossible value which will cause
		//     errors in comparisons to fail fast.)
		// 4. The list must be cyclic linked from dummy back to dummy
		//    with each next link having the opposite prev link.
		//    In particular, no prev or next links may be null.
		//    If you do this check correctly, you will not need to
		//    use the Tortoise & Hare Algorithm
		// 5. The cursor may not be null, and must be a node in the list.
		// 6. manyItems must refer to the actual number of values 
		//    in the sort sequence (obviously, not including the dummy data).
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		// (Use "report" to give a descriptive report while returning false.)
		
		// Implement conditions.
		
		//Invariant 1
		if (comparator == null) {
			return report("comparator is null.");
		}
		
		//Invariant 2
		if (dummy == null) {
			return report("dummy is null.");
		}
		
		//Invariant 3
		if (dummy.data != dummy) {
			return report("dummy's data does not refer to itself.");
		}
		
		//Invariant 4
		for (Node<E> i = dummy; dummy != null; i = i.next) {
			if (i.next == null) {
				return report("the list is not cyclic.");
			}
		}
		
		//Invariant 5
		if (cursor == null) {
			return report("cursor is null.");
		}
		else {
			Node<E> i;
			for (i = dummy; dummy != null; i = i.next) {
				if (cursor == i) {
					break;
				}
			}
			if (i != cursor) {
				return report("cursor is not in the list.");
			}
		}
		
		//Invariant 6
		int count = 0;
		for (Node<E> i = dummy; dummy != null; i = i.next) {
			if (i != null) {
				count++;
			}
		}
		if (count != manyItems) {
			return report("manyItems do not equal element or vice versa.");
		}
		
		// If no problems found, then return true:
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private SortedSequence(boolean testInvariant) { }

	/**
	 * Initialize an empty sorted sequence 
	 * using natural ordering (compareTo)
	 **/   
	public SortedSequence( )
	{
		this (null);
	}
	
	/**
	 * Initialize an empty sorted sequence
	 * @param comp comparator to use.  if null, then use
	 * natural ordering (compareTo).
	 */
	@SuppressWarnings("unchecked")// for the comparator creation
	public SortedSequence(Comparator<E> comp) {
		if (comp == null) {
			// TODO: create a new Comparator
			// in which you cast the first argument to Comparable<E>
			// so that you use compareTo.
			// (Lambda syntax will make the code shorter, but is not required.)
		}
		// TODO: Implemented by student.
		assert wellFormed() : "invariant failed at end of constructor";
	}

	// TODO:
	// Implement size/start/isCurrent/getCurrent/advance/removeCurrent/setCurrent
	// as well as insert/insertAll.
	//
	// You will need to document all methods.  You may copy text and from
	// previous assignments or their solutions from this semester,
	// but you will need update the declarations and the documentation
	// to handle our new generic situation.  In particular, don't refer to a
	// "book" in your documentation comments!
	//
	// Make sure to never use raw types!
	// Your code should not require any unchecked casts.
	//
	// You should avoid unneeded cases.
	// You may use "if" to check for error situation (throw)
	// and to check compareTo (and making sure you don't compare the dummy's data)
	// and finally for the special self-insertAll case, but otherwise
	// please avoid special cases.
	
	
	/**
	 * Size method that returns manyItems
	 * @return
	 * 		manyItems, the amount of elements in the list.
	 */
	public int size() {
		// TODO Auto-generated method stub
		return manyItems;
	}


	/**
	 * Generate a copy of this sorted sequence.
	 * @return
	 *   The return value is a copy of this sorted sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 **/ 
	@SuppressWarnings("unchecked")
	public SortedSequence<E> clone( ) { 
		assert wellFormed() : "invariant failed at start of clone";
		SortedSequence<E> answer;
	
		try
		{
			answer = (SortedSequence<E>) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}
	
		// Similar to Homework #4 but with fewer conditions.
		// (Create a new dummy node outside of loop).
		// TODO: Copy the list
		// (make sure cursor is updated too!)
	
		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant on answer failed at end of clone";
		return answer;
	}

	// don't change this nested class:
	public static class TestInternals extends LockedTestCase {
		String e1 = "A";
		String e2 = "B";
		String e3 = "C";
		String e4 = "D";
		String e5 = "E";
		String e1a = "a";
		String e3a = "c";
		String e5a = "e";
		
		SortedSequence<String> hs;
		private static Comparator<String> ascending = (s1,s2) -> s1.compareTo(s2);
		private static Comparator<String> descending = (s1,s2) -> s2.compareTo(s1);
		private static Comparator<String> nondiscrimination = (s1,s2) -> 0;

		private int reports = 0;
		

		// We have to do these operations without causing
		// Java to realize that we are lying about the dummy's data.

		@SuppressWarnings("unchecked")
		private <X,Y> void setDummyData(Node<X> node, Y value) {
			node.data = (X)value;
		}
		
		private <X> void checkDummyData(Node<X> node) {
			assertSame(node,node.data);
		}
		
		
		private void assertWellFormed(boolean expected) {
			reports = 0;
			Consumer<String> savedReporter = reporter;
			try {
				reporter = (String message) -> {
					++reports;
					if (message == null || message.trim().isEmpty()) {
						assertFalse("Uninformative report is not acceptable", true);
					}
					if (expected) {
						assertFalse("Reported error incorrectly: " + message, true);
					}
				};
				assertEquals(expected, hs.wellFormed());
				if (!expected) {
					assertEquals("Expected exactly one invariant error to be reported", 1, reports);
				}
				reporter = null;
			} finally {
				reporter = savedReporter;
			}
		}
		
		public void testA() { // checking Node constructors
			Node<?> dummy = new Node<String>();
			checkDummyData(dummy);
			assertSame(dummy,dummy.prev);
			assertSame(dummy,dummy.next);
			
			dummy = new Node<Integer>();
			checkDummyData(dummy);
			assertSame(dummy,dummy.prev);
			assertSame(dummy,dummy.next);
			
			Object value = "hello!";
			Node<Object> test = new Node<>(value);
			assertSame(value, test.data);
			assertNull(test.prev);
			assertNull(test.next);
			
			value = Integer.valueOf(1776);
			test = new Node<>(value);
			assertSame(value, test.data);
			assertNull(test.prev);
			assertNull(test.next);
		}
		
		protected <X> Node<X> newNode(X a, Node<X> p, Node<X> n) {
			Node<X> result = new Node<>(a);
			result.data = a;
			result.prev = p;
			result.next = n;
			return result;
		}
		
		protected void setUp() {
			hs = new SortedSequence<>(false);
			hs.dummy = new Node<String>();
			hs.cursor = hs.dummy;
			hs.comparator = String.CASE_INSENSITIVE_ORDER;
			hs.manyItems = 0;
		}

		public void testB() {
			assertWellFormed(true);
			
			hs.comparator = null;
			assertWellFormed(false);
			hs.comparator = nondiscrimination;
			
			hs.dummy.data = null;
			assertWellFormed(false);
			setDummyData(hs.dummy,new Node<Integer>());
			assertWellFormed(false);
			hs.dummy = null;
			assertWellFormed(false);
			hs.cursor = null;
			assertWellFormed(false);
			hs.dummy = new Node<>();
			hs.cursor = hs.dummy;
			assertWellFormed(true);
		}
		
		public void testC() {
			hs.manyItems = 1;
			assertWellFormed(false);
			hs.manyItems = 0;
			assertWellFormed(true);
			hs.manyItems = -1;
			assertWellFormed(false);
		}
		
		public void testD() {
			hs.cursor = null;
			assertWellFormed(false);
			hs.cursor = new Node<>();
			assertWellFormed(false);
			hs.cursor.prev = hs.dummy;
			hs.cursor.next = hs.dummy;
			setDummyData(hs.cursor,hs.dummy);
			assertWellFormed(false);
		}
		
		public void testE() {
			hs.manyItems = 1;
			hs.dummy.next = newNode(e1,hs.dummy,hs.dummy);
			assertWellFormed(false);
			hs.dummy.prev = hs.dummy.next;
			assertWellFormed(true);
			
			hs.manyItems = 2;
			assertWellFormed(false);
			hs.manyItems = 1;
			
			hs.comparator = null;
			assertWellFormed(false);
			hs.comparator = nondiscrimination;
			
			hs.dummy.next = null;
			assertWellFormed(false);
			hs.dummy.next = hs.dummy.prev;
			
			hs.dummy.prev.next = null;
			assertWellFormed(false);
			hs.dummy.prev.next = hs.dummy;
			
			assertWellFormed(true);
		}
				
		public void testF() {
			Node<String> n1 = newNode(e1,hs.dummy,hs.dummy);
			hs.dummy.next = hs.dummy.prev = n1;
			hs.manyItems = 1;
			hs.cursor = hs.dummy;
			assertWellFormed(true);
			
			hs.cursor = n1;
			assertWellFormed(true);
			
			hs.cursor = null;
			assertWellFormed(false);
			hs.cursor = new Node<>();
			assertWellFormed(false);
			hs.cursor.prev = n1;
			hs.cursor.next = n1;
			assertWellFormed(false);
			hs.cursor = newNode(e1,hs.dummy,hs.dummy);
			assertWellFormed(false);
			
			hs.dummy.prev = hs.dummy.next = hs.cursor;
			assertWellFormed(true);
			hs.comparator = ascending;
			assertWellFormed(true);
			hs.comparator = descending;
			assertWellFormed(true);
		}
		
		public void testG() {
			Node<String> n1 = newNode(e1,hs.dummy,hs.dummy);
			hs.dummy.next = hs.dummy.prev = n1;
			hs.manyItems = 1;
			hs.cursor = hs.dummy;
			assertWellFormed(true);
			
			Node<String> n1a = newNode(e1,hs.dummy,hs.dummy);
			Node<String> n0a = new Node<>();
			n0a.next = n0a.next = n1;
			
			hs.dummy.prev = null;
			assertWellFormed(false);
			hs.dummy.prev = n1a;
			assertWellFormed(false);
			hs.dummy.prev = hs.dummy;
			assertWellFormed(false);
			hs.dummy.prev = n1;
			
			n1.prev = null;
			assertWellFormed(false);
			n1.prev = n0a;
			assertWellFormed(false);
			n1.prev = hs.dummy;
			
			assertWellFormed(true);
		}
		
		public void testH() {
			Node<String> n1 = newNode(e1,hs.dummy,null);
			Node<String> n2 = newNode(e2,n1,hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			hs.dummy.prev = n2;
			hs.manyItems = 2;
			assertWellFormed(true);
			
			hs.manyItems = 1;
			assertWellFormed(false);
			hs.manyItems = 3;
			assertWellFormed(false);
			hs.manyItems = 0;
			assertWellFormed(false);
		}
		
		public void testI() {
			Node<String> n1 = newNode(e1,hs.dummy,null);
			Node<String> n2 = newNode(e2,n1,hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			hs.dummy.prev = n2;
			hs.manyItems = 2;
			assertWellFormed(true);
			
			hs.cursor = new Node<>();
			hs.cursor.prev = n2;
			hs.cursor.next = n1;
			assertWellFormed(false);
			hs.cursor = null;
			assertWellFormed(false);
			hs.cursor = newNode(e1,hs.dummy,n2);
			assertWellFormed(false);
			hs.cursor = newNode(e2,n1,hs.dummy);
			assertWellFormed(false);
			
			hs.cursor = n1;
			assertWellFormed(true);
			hs.cursor = n2;
			assertWellFormed(true);
			
			hs.manyItems = 3;
			assertWellFormed(false);
		}
		
		public void testJ() {
			Node<String> n1 = newNode(e1,hs.dummy,null);
			Node<String> n2 = newNode(e2,n1,hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			hs.dummy.prev = n2;
			hs.manyItems = 2;
			assertWellFormed(true);
			
			Node<String> n1a = newNode(e1,hs.dummy,n2);
			Node<String> n2a = newNode(e2,n1,hs.dummy);
			Node<String> n0a = newNode(null,n2,n1);
			setDummyData(n0a,hs.dummy);

			hs.dummy.prev = null;
			assertWellFormed(false);
			hs.dummy.prev = n1;
			assertWellFormed(false);
			hs.dummy.prev = hs.dummy;
			assertWellFormed(false);
			hs.dummy.prev = n2a;
			assertWellFormed(false);
			hs.dummy.prev = n2;
			
			n1.prev = n1;
			assertWellFormed(false);
			n1.prev = n2;
			assertWellFormed(false);
			n1.prev = n0a;
			assertWellFormed(false);
			n1.prev = null;
			assertWellFormed(false);
			n1.prev = hs.dummy;

			n2.prev = hs.dummy;
			assertWellFormed(false);
			n2.prev = n1a;
			assertWellFormed(false);
			n2.prev = n2;
			assertWellFormed(false);
			n2.prev = null;
			assertWellFormed(false);
			n2.prev = n1;
			
			assertWellFormed(true);
		}
		
		public void testK() {
			Node<String> n1 = newNode(e1,hs.dummy,null);
			Node<String> n2 = newNode(e2,n1,hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			hs.dummy.prev = n2;
			hs.manyItems = 2;
			assertWellFormed(true);
			
			hs.comparator = null;
			assertWellFormed(false);
			hs.comparator = descending;
			assertWellFormed(false);
			hs.comparator = ascending;
			assertWellFormed(true);
			hs.comparator = nondiscrimination;
			assertWellFormed(true);
			
			n2.data = null;
			assertWellFormed(false);
			n2.data = e2;
			n1.data = null;
			assertWellFormed(false);
		}
		
		public void testL() {
			Node<String> n1 = newNode(e1, hs.dummy, null);
			Node<String> n2 = newNode(e2, n1, null);
			Node<String> n3 = newNode(e3, n2, null);
			Node<String> n4 = newNode(e4, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.dummy.prev = n5;
			
			hs.manyItems = 1;
			assertWellFormed(false);
			hs.manyItems = 2;
			assertWellFormed(false);
			hs.manyItems = 3;
			assertWellFormed(false);
			hs.manyItems = 4;
			assertWellFormed(false);
			hs.manyItems = 0;
			assertWellFormed(false);
			hs.manyItems = 6;
			assertWellFormed(false);
			hs.manyItems = 5;
			assertWellFormed(true);
		}
		
		public void testM() {
			Node<String> n1 = newNode(e1, hs.dummy, null);
			Node<String> n2 = newNode(e2, n1, null);
			Node<String> n3 = newNode(e3, n2, null);
			Node<String> n4 = newNode(e4, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.manyItems = 5;
			hs.dummy.prev = n5;
			assertWellFormed(true);
			
			hs.cursor = n1;
			assertWellFormed(true);
			hs.cursor = n2;
			assertWellFormed(true);
			hs.cursor = n3;
			assertWellFormed(true);
			hs.cursor = n4;
			assertWellFormed(true);
			hs.cursor = n5;
			assertWellFormed(true);
		}
		
		public void testN() {
			Node<String> n1 = newNode(e1, hs.dummy, null);
			Node<String> n2 = newNode(e2, n1, null);
			Node<String> n3 = newNode(e3, n2, null);
			Node<String> n4 = newNode(e4, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.dummy.prev = n5;
			hs.manyItems = 5;
			assertWellFormed(true);

			
			hs.cursor = null;
			assertWellFormed(false);
			hs.cursor = new Node<>();
			assertWellFormed(false);
			hs.cursor = newNode(e1, hs.dummy, n2);
			assertWellFormed(false);
			hs.cursor = newNode(e2, n1, n3);
			assertWellFormed(false);
			hs.cursor = newNode(e3, n2, n4);
			assertWellFormed(false);
			hs.cursor = newNode(e4, n3, n5);
			assertWellFormed(false);
			hs.cursor = newNode(e5, n4, hs.dummy);
			assertWellFormed(false);
		}
		
		public void testO() {
			Node<String> n1 = newNode(e5, hs.dummy, null);
			Node<String> n2 = newNode(e5, n1, null);
			Node<String> n3 = newNode(e5, n2, null);
			Node<String> n4 = newNode(e5, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.dummy.prev = n5;
			hs.manyItems = 5;
			assertWellFormed(true);

			hs.comparator = ascending;
			assertWellFormed(true);
			hs.comparator = descending;
			assertWellFormed(true);
			hs.comparator = nondiscrimination;
			assertWellFormed(true);			
		}
		
		public void testP() {
			Node<String> n1 = newNode(e1, hs.dummy, null);
			Node<String> n2 = newNode(e2, n1, null);
			Node<String> n3 = newNode(e3, n2, null);
			Node<String> n4 = newNode(e4, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.dummy.prev = n5;
			hs.manyItems = 5;
			assertWellFormed(true);
			
			n5.data = null;
			assertWellFormed(false);
			
			n5.data = e3;
			assertWellFormed(false);
			
			n4.data = e3;
			assertWellFormed(true);
			
			n4.data = null;
			assertWellFormed(false);
			
			n4.data = e2;
			assertWellFormed(false);
			
			n3.data = e2;
			assertWellFormed(true);
			
			n3.data = null;
			assertWellFormed(false);

			n3.data = e1;
			assertWellFormed(false);
			
			n2.data = e1;
			assertWellFormed(true);
						
			n2.data = null;
			assertWellFormed(false);
		}
		
		public void testQ() {
			Node<String> n1 = newNode(e1, hs.dummy, null);
			Node<String> n2 = newNode(e2, n1, null);
			Node<String> n3 = newNode(e3, n2, null);
			Node<String> n4 = newNode(e4, n3, null);
			Node<String> n5 = newNode(e5, n4, hs.dummy);
			hs.dummy.next = n1;
			n1.next = n2;
			n2.next = n3;
			n3.next = n4;
			n4.next = n5;
			hs.dummy.prev = n5;
			hs.manyItems = 5;
			assertWellFormed(true);

			n1.data = e1a;
			assertWellFormed(true);
			n3.data = e3a;
			assertWellFormed(true);
			n5.data = e5a;
			assertWellFormed(true);
			
			hs.comparator = ascending;
			assertWellFormed(false);
			hs.comparator = descending;
			assertWellFormed(false);
			hs.comparator = nondiscrimination;
			assertWellFormed(true);
		}
	}
}

