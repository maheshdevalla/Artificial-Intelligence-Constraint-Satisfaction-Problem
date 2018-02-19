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
package mapcoloring;

import java.util.*;

class MapColorConstraints {
	private ArrayList<Integer> constraintvalues = new ArrayList<Integer>();
	private static Hashtable<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> constraintvaluestable = new Hashtable<>();

	public ArrayList<Integer> getConstraintvalues() {
		return constraintvalues;
	}

	public void setConstraintvalues(ArrayList<Integer> constraintvalues) {
		this.constraintvalues = constraintvalues;
	}

	public static Hashtable<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> getConstraintvaluestable() {
		return constraintvaluestable;
	}

	public static void setConstraintvaluestable(
			Hashtable<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> constraintvaluestable) {
		MapColorConstraints.constraintvaluestable = constraintvaluestable;
	}

	private ArrayList<Integer> constraints = new ArrayList<Integer>();

	public ArrayList<Integer> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<Integer> constraints) {
		this.constraints = constraints;
	}

	// constructor to assign values accordingly.
	MapColorConstraints(ArrayList<Integer> constraintvalues, ArrayList<Integer> constraints) {
		boolean flag = false;
		this.constraintvalues = constraintvalues;
		this.constraints = constraints;
		for (ArrayList<Integer> loopar : constraintvaluestable.keySet()) {
			if (loopar.equals(constraintvalues)) {
				flag = true;
				constraintvaluestable.get(loopar).add(constraints);
			}
		}
		if (flag == false) {
			ArrayList<ArrayList<Integer>> tempconstraints = new ArrayList<>();
			tempconstraints.add(constraints);
			constraintvaluestable.put(constraintvalues, tempconstraints);
		}
	}

	public boolean checkConstraints(ArrayList<Integer> constraint) {
		ArrayList<Integer> tempvalues = new ArrayList<>();

		for (int temp : constraintvalues) {
			if (constraint.get(temp) == -1) {
				return true;
			}
		}

		for (int i = 0; i < constraintvalues.size(); i++) {
			tempvalues.add(constraint.get(constraintvalues.get(i)));
		}
		return constraintvaluestable.get(constraintvalues).contains(tempvalues) ? true : false;
	}

	@Override
	public String toString() {
		return "Constraints are" + constraintvalues.toString() + constraints.toString();
	}

}
