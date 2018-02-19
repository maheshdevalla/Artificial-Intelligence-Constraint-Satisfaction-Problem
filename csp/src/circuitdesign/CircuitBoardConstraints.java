/*
 * Author: Mahesh Devalla
 * Artificial Intelligence
 * Assignment: Constraint Satisfaction Problem
 * 
 * References:
 * https://courses.cs.washington.edu/courses/cse573/06au/slides/chapter05-6pp.pdf
 * http://pami.uwaterloo.ca/~basir/ECE457/week5.pdf
 * http://aima.cs.berkeley.edu/newchap05.pdf
 * http://www.cs.dartmouth.edu/~devin/cs76/05_constraint/constraint.html
 * https://www.ics.uci.edu/~welling/teaching/271fall09/CSPpaper.pdf
 * https://ocw.mit.edu/courses/electrical-engineering-and-computer-science/6-034-artificial-intelligence-spring-2005/lecture-notes/ch3_csp_games1.pdf
 * http://embedded.eecs.berkeley.edu/Alumni/wray/complex/complex/node16.html
 * http://www.cs.cmu.edu/~cga/ai-course/constraint.pdf
 * 
 * 
 */
package circuitdesign;

import javafx.util.Pair;

class CircuitBoardConstraints {
	private Pair<Integer, Integer> coordinates;

	public CircuitBoardConstraints(int length, int breadth) {
		coordinates = new Pair<Integer, Integer>(length, breadth);
	}

	/*
	 * To get the length of the circuit board
	 */
	public int getLength() {
		return coordinates.getKey();
	}

	/*
	 * To get the breadth of the circuit board
	 */
	public int getBreadth() {
		return coordinates.getValue();
	}

	// code to return area but just overloaded hashcode method.
	public int hashCode() {
		int length = getLength();
		int breadth = getBreadth();
		return (length * 100) + breadth;
	}

	// http://quiz.geeksforgeeks.org/pair-class-in-java/
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getLength() == ((CircuitBoardConstraints) obj).getLength()
				&& getBreadth() == ((CircuitBoardConstraints) obj).getBreadth()) {
			return true;
		}
		return false;
	}
}