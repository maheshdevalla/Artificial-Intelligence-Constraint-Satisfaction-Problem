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

class ConstraintSatisfaction {
	private boolean cspc = false;

	public boolean isCspc() {
		return cspc;
	}

	public void setCspc(boolean cspc) {
		this.cspc = cspc;
	}

	private boolean MRV = true;
	private int noOfCons;

	public int getNoOfCons() {
		return noOfCons;
	}

	public void setNoOfCons(int noOfCons) {
		this.noOfCons = noOfCons;
	}

	private Map<Integer, Set<Integer>> baselist;

	public Map<Integer, Set<Integer>> getBaselist() {
		return baselist;
	}

	public void setBaselist(Map<Integer, Set<Integer>> baselist) {
		this.baselist = baselist;
	}

	private Map<CircuitBoardConstraints, Set<CircuitBoardConstraints>> constraints;

	public Map<CircuitBoardConstraints, Set<CircuitBoardConstraints>> getConstraints() {
		return constraints;
	}

	public void setConstraints(Map<CircuitBoardConstraints, Set<CircuitBoardConstraints>> constraints) {
		this.constraints = constraints;
	}

	private ArrayList<Map<Integer, Set<Integer>>> unlist;
	private ArrayList<Map<Integer, Integer>> path;

	public ArrayList<Map<Integer, Set<Integer>>> getUnlist() {
		return unlist;
	}

	public void setUnlist(ArrayList<Map<Integer, Set<Integer>>> unlist) {
		this.unlist = unlist;
	}

	public ArrayList<Map<Integer, Integer>> getPath() {
		return path;
	}

	public void setPath(ArrayList<Map<Integer, Integer>> path) {
		this.path = path;
	}

	public boolean isMRV() {
		return MRV;
	}

	public void setMRV(boolean mRV) {
		MRV = mRV;
	}

	/*
	 * Constructor to add the required to constraints to circuit.
	 */
	public ConstraintSatisfaction() {
		noOfCons = 0;
		baselist = new HashMap<Integer, Set<Integer>>();
		constraints = new HashMap<CircuitBoardConstraints, Set<CircuitBoardConstraints>>();
		unlist = new ArrayList<Map<Integer, Set<Integer>>>();
		path = new ArrayList<Map<Integer, Integer>>();
		unlist.add(new HashMap<Integer, Set<Integer>>());
		path.add(new HashMap<Integer, Integer>());
	}

	/*
	 * A function to solve CSP circuit
	 */
	public Map<Integer, Integer> solve() throws Exception {
		noOfCons = 0;
		Map<Integer, Integer> solution = SolvingConstraint(new HashMap<>(), 0);
		System.out.println("Number of constraints checked in the Circuit Board are:" + noOfCons);
		return solution;
	}

	// Adding the constraints to the CSP
	public void addValue(int v, Set<Integer> domain) {
		Set<Integer> temp = new HashSet<Integer>(domain);
		this.baselist.put(v, temp);
	}

	public void addConstraints(Integer val1, Integer val2, Set<CircuitBoardConstraints> assignval) {
		CircuitBoardConstraints cons = new CircuitBoardConstraints(val1, val2);
		this.constraints.put(cons, assignval);
	}

	/*
	 * Function that solves the problem of CSP for Circuit board i.e. backtrack
	 * algorithm
	 */
	public Map<Integer, Integer> SolvingConstraint(Map<Integer, Integer> map, int limit) throws Exception {
		Set<Integer> listOfCons;
		Map<Integer, Integer> temppath = path.get(limit);
		Map<Integer, Set<Integer>> tempunlist = unlist.get(limit);
		Integer ucons = randCons(map);
		if (ucons == -1)
			return map;
		listOfCons = baselist.get(ucons);
		int[] templistOfCons = new int[listOfCons.size()];
		int i = 0;
		for (int j : listOfCons) {
			templistOfCons[i] = j;
			i++;
		}

		int listcnt = templistOfCons.length;
		for (i = 0; i < listcnt; i++) {
			int j = templistOfCons[i];
			map.put(ucons, j);
			temppath.put(ucons, j);
			if (MAC(ucons, j, map, tempunlist, temppath)) {
				unlist.add(new HashMap<Integer, Set<Integer>>());
				path.add(new HashMap<Integer, Integer>());
				Map<Integer, Integer> mapcircuit = SolvingConstraint(map, limit++);
				if (mapcircuit != null) {
					return mapcircuit;
				}
			}
			Set<Integer> redudlist = new HashSet<Integer>(temppath.keySet());
			for (int red : redudlist) {
				map.remove(red);
			}

			Set<Integer> unlistset = new HashSet<Integer>(tempunlist.keySet());
			for (int unset : unlistset) {
				Set<Integer> cordx = tempunlist.get(unset);
				for (Integer tempcordx : cordx) {
					this.baselist.get(unset).add(tempcordx);
				}
				tempunlist.remove(unset);
			}
			tempunlist = new HashMap<Integer, Set<Integer>>();
			temppath = new HashMap<Integer, Integer>();
		}

		if (limit >= 1) {
			unlist.remove(unlist.size() - 1);
			path.remove(unlist.size() - 1);
		}
		return null;
	}

	public int randCons(Map<Integer, Integer> cspcir) {
		if (!isCspc()) {
			for (int temp : baselist.keySet()) {
				if (cspcir.get(temp) == null)
					return temp;
			}
		} else {
			if (isMRV()) {
				int maxVal = Integer.MAX_VALUE;
				int minVal = 0;
				for (int temp : baselist.keySet()) {
					if (cspcir.get(temp) == null)
						return temp;
					if (cspcir.get(temp) < maxVal) {
						maxVal = cspcir.get(temp);
						minVal = temp;
					}
				}
				return minVal;
			}
		}
		return -1;
	}

	// Function for MAC3 for inference to check the values
	public boolean MAC(int v1, int v2, Map<Integer, Integer> cspcircuit, Map<Integer, Set<Integer>> unlist,
			Map<Integer, Integer> listOfCons) {
		int constraint, consval;
		Set<Integer> templist;
		Set<CircuitBoardConstraints> circuit;
		Iterator<Integer> itr;
		CircuitBoardConstraints tempConstraint;
		for (int i : getBaselist().keySet()) {
			if (baselist.get(i).contains(v2)) {
				baselist.get(i).remove(v2);
				if (unlist.containsKey(i)) {
					unlist.get(i).add(v2);
				} else {
					unlist.put(i, new HashSet<Integer>());
					unlist.get(i).add(v2);
				}
			}
		}

		for (int i : getBaselist().get(v1)) {
			if (i != v2) {
				if (unlist.containsKey(v1)) {
					unlist.get(v1).add(i);
				} else {
					unlist.put(v1, new HashSet<Integer>());
					unlist.get(v1).add(i);
				}
			}
		}
		getBaselist().put(v1, new HashSet<Integer>());
		getBaselist().get(v1).add(v2);

		for (CircuitBoardConstraints tempcons : constraints.keySet()) {
			noOfCons++;
			if (tempcons.getLength() == v1) {
				int tempVar = tempcons.getBreadth();
				if (!cspcircuit.containsKey(tempVar)) {
					circuit = constraints.get(tempcons);

					itr = getBaselist().get(tempVar).iterator();

					while (itr.hasNext()) {
						consval = itr.next();
						tempConstraint = new CircuitBoardConstraints(v2, consval);

						if (!circuit.contains(tempConstraint)) {
							itr.remove();
							if (unlist.containsKey(tempVar)) {
								unlist.get(tempVar).add(consval);
							} else {
								unlist.put(tempVar, new HashSet<Integer>());
								unlist.get(tempVar).add(consval);
							}
						}

					}
				}
			} else if (tempcons.getBreadth() == v1) {
				constraint = tempcons.getLength();

				if (!cspcircuit.containsKey(constraint)) {
					circuit = constraints.get(tempcons);

					itr = getBaselist().get(constraint).iterator();

					while (itr.hasNext()) {
						consval = itr.next();
						tempConstraint = new CircuitBoardConstraints(consval, v2);

						if (!circuit.contains(tempConstraint)) {
							itr.remove();

							if (unlist.containsKey(constraint)) {
								unlist.get(constraint).add(consval);
							} else {
								unlist.put(constraint, new HashSet<Integer>());
								unlist.get(constraint).add(consval);
							}
						}
					}
				}
			}
		}

		for (int temp : baselist.keySet()) {
			if (!cspcircuit.containsKey(temp)) {
				templist = baselist.get(temp);

				if (templist.size() == 0) {
					return false;
				}

				if (templist.size() == 1) {
					itr = templist.iterator();
					Integer nextValue = itr.next();

					cspcircuit.put(temp, nextValue);
					listOfCons.put(temp, nextValue);

					if (!MAC(temp, nextValue, cspcircuit, unlist, listOfCons))
						return false;
				}
			}
		}
		return true;
	}

}
