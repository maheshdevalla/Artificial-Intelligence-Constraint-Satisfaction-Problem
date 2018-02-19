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

import java.util.*;

class CircuitBoard {
	public ConstraintSatisfaction getCsp() {
		return csp;
	}

	public void setCsp(ConstraintSatisfaction csp) {
		this.csp = csp;
	}

	public int getCblength() {
		return cblength;
	}

	public void setCblength(int cblength) {
		this.cblength = cblength;
	}

	public int getCbbreadth() {
		return cbbreadth;
	}

	public void setCbbreadth(int cbbreadth) {
		this.cbbreadth = cbbreadth;
	}

	public Design[] getDesign() {
		return design;
	}

	private ConstraintSatisfaction csp = new ConstraintSatisfaction();
	private int cblength, cbbreadth;
	private Design[] design;

	public static class Design {
		public int dlength, dbreadth;
		public char object;

		public Design(int dlength, int dbreadth, char object) {
			this.dlength = dlength;
			this.dbreadth = dbreadth;
			this.object = object;
		}
	}

	public CircuitBoard(int cblength, int cbbreadth, Design[] object) {
		this.cblength = cblength;
		this.cbbreadth = cbbreadth;
		this.design = object;

		/*
		 * Assigning the constraints for the CSP circuit
		 */
		for (int i = 0; i < design.length; i++) {
			Set<Integer> set = new HashSet<>();
			for (int j = 0; j <= getCbbreadth() - design[i].dlength; j++)

				for (int k = 0; k <= getCblength() - design[i].dbreadth; k++)

					set.add((k * getCbbreadth()) + j);

			csp.addValue(i, set);
		}

		/*
		 * Designing all the constraints to satisfy
		 */
		for (int i = 0; i < design.length - 1; i++) {
			for (int j = i + 1; j < design.length; j++) {
				Set<CircuitBoardConstraints> constraints = new HashSet<>();
				for (int temp1 : csp.getBaselist().get(i)) {
					for (int temp2 : csp.getBaselist().get(j)) {
						if (!isValid(design[i], temp1, design[j], temp2))
							constraints.add(new CircuitBoardConstraints(temp1, temp2));
					}
				}
				csp.addConstraints(i, j, constraints);
			}
		}

	}

	/*
	 * A function to check the constraints i.e. the objects on the circuit are
	 * aligned correctly or not
	 */

	public boolean isValid(Design design1, int object1, Design design2, int object2) {
		Set<Integer> emptyspace1 = new HashSet<>();
		Set<Integer> emptyspace2 = new HashSet<>();

		int cordx = object1 % getCbbreadth();
		int cordy = object1 / getCbbreadth();

		for (int i = cordy; i < (cordy + design1.dbreadth); i++) {
			for (int j = cordx; j < (cordx + design1.dlength); j++) {
				emptyspace1.add(i * getCbbreadth() + j);
			}
		}
		cordx = object2 % getCbbreadth();
		cordy = object2 / getCbbreadth();
		for (int k = cordy; k < (cordy + design2.dbreadth); k++)
			for (int l = cordx; l < (cordx + design2.dlength); l++)
				emptyspace2.add(k * getCbbreadth() + l);
		emptyspace1.retainAll(emptyspace2);
		return (!emptyspace1.isEmpty());
	}

	// Refernce from
	// http://embedded.eecs.berkeley.edu/Alumni/wray/complex/complex/node16.html
	public char[] solve() throws Exception {
		Map<Integer, Integer> map = csp.solve();
		int area = cblength * cbbreadth;
		char[] ch = new char[area];
		for (int i = 0; i < area; i++)
			ch[i] = '*';
		for (int i : map.keySet()) {
			int values = map.get(i);
			Design tempdesign = design[i];
			int cordx = values % getCbbreadth();
			int cordy = values / getCbbreadth();
			for (int j = cordy; j < (cordy + tempdesign.dbreadth); j++)
				for (int k = cordx; k < (cordx + tempdesign.dlength); k++)
					ch[j * getCbbreadth() + k] = tempdesign.object;
		}
		return ch;
	}

	public static final void main(String[] args) throws Exception {
		Design[] design = new Design[4];
		design[0] = new Design(3, 2, 'A');
		design[1] = new Design(5, 2, 'B');
		design[2] = new Design(2, 3, 'C');
		design[3] = new Design(7, 1, 'E');

		CircuitBoard finalcircuit = new CircuitBoard(3, 10, design);
		char[] ch = finalcircuit.solve();
		if (ch == null)
			System.out.println("Cannot deisgn a circuit with the given conditions");

		int i = 0;
		System.out.println("Circuit designed with the given constraints are:");
		for (int y = 0; y < finalcircuit.cblength; y++) {
			for (int x = 0; x < finalcircuit.cbbreadth; x++) {
				System.out.print(ch[i]);
				i++;
				// System.out.print("\n");
			}
			System.out.println();
		}
	}
}