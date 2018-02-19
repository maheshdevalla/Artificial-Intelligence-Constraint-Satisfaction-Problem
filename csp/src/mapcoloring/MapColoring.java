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

import java.util.ArrayList;
import java.util.Arrays;

class MapColoring extends ConstraintSatisfaction {

	private ArrayList<String> state, statecolor;

	public ArrayList<String> getState() {
		return state;
	}

	public void setState(ArrayList<String> state) {
		this.state = state;
	}

	public ArrayList<String> getStatecolor() {
		return statecolor;
	}

	public void setStatecolor(ArrayList<String> statecolor) {
		this.statecolor = statecolor;
	}
	/*
	 * The constraints here are the states of Australia
	 */

	MapColoring(ArrayList<String> state, ArrayList<String> statecolor, ArrayList<MapColorConstraints> constraints) {
		// Assigning these values to the CSP MAP
		super(state.size(), statecolor.size(), constraints);
		this.state = state;
		this.statecolor = statecolor;
	}

	public MapColorConstraints addConstraints(ArrayList<String> tempConstStates, ArrayList<String> tempStateColor) {
		ArrayList<Integer> state = new ArrayList<>();
		ArrayList<Integer> statecolor = new ArrayList<>();
		for (String tempstate : tempConstStates) {
			int i = getState().indexOf(tempstate);
			state.add(i);
		}

		for (String tempstatecolor : tempStateColor) {
			int j = getStatecolor().indexOf(tempstatecolor);
			statecolor.add(j);
		}

		MapColorConstraints ret_cons = new MapColorConstraints(state, statecolor);
		this.getConstraints().add(ret_cons);
		return ret_cons;

	}

	protected void addStateConstraints() {

		/*
		 * Basic idea taken from http://aima.cs.berkeley.edu/newchap05.pdf
		 * https://courses.cs.washington.edu/courses/cse573/06au/slides/
		 * chapter05-6pp.pdf
		 */
		ArrayList<ArrayList<String>> listofstates = new ArrayList<>();
		// Adding adjacent states of South Australia and its adjacent states in
		// reverse order for ease of access.
		ArrayList<String> SAandV = new ArrayList<>(Arrays.asList("SA", "V"));
		ArrayList<String> SAandNSW = new ArrayList<>(Arrays.asList("SA", "NSW"));
		ArrayList<String> SAandQ = new ArrayList<>(Arrays.asList("SA", "Q"));
		ArrayList<String> SAandNT = new ArrayList<>(Arrays.asList("SA", "NT"));
		ArrayList<String> SAandWA = new ArrayList<>(Arrays.asList("SA", "WA"));

		// Adding Western Australia and its adjacent states(Already a state
		// added removing duplicate)
		ArrayList<String> WAandNT = new ArrayList<>(Arrays.asList("WA", "NT"));
		// Adding Northern Territory and its adjacent states(Already a state
		// added removing duplicate)
		ArrayList<String> NTandQ = new ArrayList<>(Arrays.asList("NT", "Q"));
		// Adding Queensland and its adjacent states(Already a state added
		// removing duplicate)
		ArrayList<String> QandNSW = new ArrayList<>(Arrays.asList("Q", "NSW"));
		// Adding New South Wales and its adjacent states(Already a state added
		// removing duplicate)
		ArrayList<String> NSWandV = new ArrayList<>(Arrays.asList("NSW", "V"));
		// Adding all states as list
		listofstates
				.addAll(Arrays.asList(SAandV, SAandNSW, SAandQ, SAandNT, SAandWA, WAandNT, NTandQ, QandNSW, NSWandV));

		/*
		 * Color combinations taken as example from
		 * http://www.cs.dartmouth.edu/~devin/cs76/05_constraint/constraint.html
		 */

		ArrayList<ArrayList<String>> statecolors = new ArrayList<>();
		ArrayList<String> GreenandBlue = new ArrayList<>(Arrays.asList("GREEN", "BLUE"));
		ArrayList<String> GreenandRed = new ArrayList<>(Arrays.asList("GREEN", "RED"));
		ArrayList<String> RedandBlue = new ArrayList<>(Arrays.asList("RED", "BLUE"));
		ArrayList<String> RedandGreen = new ArrayList<>(Arrays.asList("RED", "GREEN"));
		ArrayList<String> BlueandRed = new ArrayList<>(Arrays.asList("BLUE", "RED"));
		ArrayList<String> BlueandGreen = new ArrayList<>(Arrays.asList("BLUE", "GREEN"));
		statecolors.addAll(Arrays.asList(RedandGreen, RedandBlue, GreenandRed, GreenandBlue, BlueandGreen, BlueandRed));

		// Make constraints
		int constraints = 0;
		for (ArrayList<String> temp : listofstates) {
			for (ArrayList<String> two_colors : statecolors) {
				this.getConstraints().add(addConstraints(temp, two_colors));
			}
			constraints++;
		}
	}

	public static void main(String args[]) throws Exception {
		ArrayList<String> listofstates = new ArrayList<>();
		ArrayList<Integer> csp;
		ArrayList<MapColorConstraints> constraints = new ArrayList<MapColorConstraints>();
		ArrayList<String> map = new ArrayList<String>(Arrays.asList("WA", "NT", "Q", "NSW", "V", "SA", "T"));
		ArrayList<String> statecolors = new ArrayList<String>(Arrays.asList("GREEN", "RED", "BLUE"));
		MapColoring statecolorcsp = new MapColoring(map, statecolors, constraints);
		statecolorcsp.addStateConstraints();
		System.out.println("Hashtable representing the states of Australia are: \n"
				+ MapColorConstraints.getConstraintvaluestable().toString());
		csp = statecolorcsp.Search();
		for (int i = 0; i < csp.size(); i++) {
			String state = statecolorcsp.getState().get(i);
			state += ":";
			state += statecolorcsp.statecolor.get(csp.get(i));
			listofstates.add(state);
		}
		System.out.println("\nThe colors represented in the Australia map after satisfying the constraints with three colors are:");
		System.out.println(listofstates);
	}
}
