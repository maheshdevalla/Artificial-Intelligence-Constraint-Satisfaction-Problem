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

class ConstraintSatisfaction {
	private ArrayList<Integer> baseList = new ArrayList<>();
	private ArrayList<Integer> constraintlist = new ArrayList<>();
	private ArrayList<MapColorConstraints> constraints = new ArrayList<>();

	public ArrayList<Integer> getBaseList() {
		return baseList;
	}

	public void setBaseList(ArrayList<Integer> baseList) {
		this.baseList = baseList;
	}

	public ArrayList<Integer> getConstraintlist() {
		return constraintlist;
	}

	public void setConstraintlist(ArrayList<Integer> constraintlist) {
		this.constraintlist = constraintlist;
	}

	public ArrayList<MapColorConstraints> getConstraints() {
		return constraints;
	}

	public void setConstraints(ArrayList<MapColorConstraints> constraints) {
		this.constraints = constraints;
	}

	/*
	 * Heuristics that are defined to enable or disable according to the
	 * assignment. Variables used to compare effectiveness of the output.
	 * http://www.cs.dartmouth.edu/~devin/cs76/05_constraint/constraint.html
	 * 
	 */
	private boolean MRV = false;
	private boolean LCV = false;
	private boolean MAC_3 = false;

	public boolean isMRV() {
		return MRV;
	}

	public void setMRV(boolean mRV) {
		MRV = mRV;
	}

	public boolean isLCV() {
		return LCV;
	}

	public void setLCV(boolean lCV) {
		LCV = lCV;
	}

	public boolean isMAC_3() {
		return MAC_3;
	}

	public void setMAC_3(boolean mAC_3) {
		MAC_3 = mAC_3;
	}

	/*
	 * Variable to enable or disable interface
	 * http://www.cs.dartmouth.edu/~devin/cs76/05_constraint/constraint.html
	 * 
	 */

	private ArrayList<ArrayList<Integer>> listconsraints = new ArrayList<>();
	private ArrayList<Path> allconstrpath = new ArrayList<>();

	public ArrayList<ArrayList<Integer>> getListconsraints() {
		return listconsraints;
	}

	public ArrayList<Path> getAllconstrpath() {
		return allconstrpath;
	}

	public void setAllconstrpath(ArrayList<Path> allconstrpath) {
		this.allconstrpath = allconstrpath;
	}

	public void setListconsraints(ArrayList<ArrayList<Integer>> listconsraints) {
		this.listconsraints = listconsraints;
	}

	/*
	 * Using list as the main source to assign values
	 */
	@SuppressWarnings("unchecked")
	ConstraintSatisfaction(int varibles, int values, ArrayList<MapColorConstraints> cList) {
		for (int i = 0; i < values; i++) {
			constraintlist.add(i);
		}
		// Assigning values to the list
		for (int i = 0; i < varibles; i++) {
			baseList.add(-1);
			ArrayList<Integer> constraintValues = (ArrayList<Integer>) constraintlist.clone();
			listconsraints.add(constraintValues);
		}
		// Assigning constraints
		constraints.addAll(cList);

		for (MapColorConstraints tempcList : cList) {
			int firstpath = tempcList.getConstraintvalues().get(0);
			int lastpath = tempcList.getConstraintvalues().get(1);
			allconstrpath.add(new Path(firstpath, lastpath));
			allconstrpath.add(new Path(lastpath, firstpath));
		}

	}

	/*
	 * Search function to backtrack the path
	 */
	public ArrayList<Integer> Search() throws Exception {
		int nextassign;

		if (!baseList.contains(-1)) {
			return baseList;
		}

		if (isMRV()) {
			nextassign = getMRV(baseList);
		} else {
			int temp = baseList.indexOf(-1);
			if (temp == -1) {
				System.out.println("This assignment is not valid");
				System.exit(-1);
			}
			nextassign = temp;
		}
		ArrayList<Integer> var = getOrgValues(nextassign, baseList);
		// Assigning only correct values
		for (int val : var) {
			baseList.set(nextassign, val);
			if (checkConstraints(baseList)) {
				if (isMAC_3()) {
					boolean flag = MAC3(nextassign, baseList);
					if (flag == true) {
						ArrayList<Integer> res = Search();
						if (res != null) {
							return res;
						}
					}
				} else {
					ArrayList<Integer> res = Search();
					;
					if (res != null) {
						return res;
					}
				}
				baseList.set(nextassign, -1);
			}
		}
		return null;
	}

	// To check the assigned constraints
	public boolean checkConstraints(ArrayList<Integer> arr) {
		boolean flag = true;
		for (MapColorConstraints constraint : constraints) {
			if (!constraint.checkConstraints(arr)) {
				flag = false;
			}
		}
		return flag;
	}

	/*
	 * A function to return all the possible values
	 */

	public int getVal(int v, ArrayList<Integer> arr) {

		int temp = 0;
		if (arr.get(v) != -1) {
			return Integer.MAX_VALUE;
		}
		for (int i : listconsraints.get(v)) {
			int temp2 = arr.get(v);
			arr.set(v, i);
			if (checkConstraints(arr)) {
				temp++;
			}
			arr.set(v, temp2);
		}
		if (temp == 0) {
			temp = Integer.MAX_VALUE;
		}

		return temp;
	}

	public int getMRV(ArrayList<Integer> arr) {
		int mrv = 0;
		int mrvv = Integer.MAX_VALUE;
		for (int i = 0; i < arr.size(); i++) {
			int temp = getVal(i, arr);
			if (temp < mrvv) {
				mrv = i;
				mrvv = temp;
			}
		}
		return mrv;
	}

	/*
	 * A function for the implementation of LCV
	 */
	public ArrayList<Integer> getOrgValues(int v, ArrayList<Integer> assignval) {
		if (isLCV()) {
			ArrayList<Integer> listLCV = new ArrayList<Integer>();
			ArrayList<Integer> nextVals = nextVals(v);
			int prev = assignval.get(v);
			ArrayList<Integer> otrval = new ArrayList<>();
			for (int i : listconsraints.get(v)) {
				assignval.set(v, i);
				otrval.add(i, 0);
				for (int n_var : nextVals) {
					int tempval = getVal(n_var, assignval);
					otrval.set(i, otrval.get(i) + tempval);
				}
				assignval.set(v, prev);
			}
			for (int i = 0; i < otrval.size(); i++) {
				int tempmax = otrval.indexOf(Collections.min(otrval));
				listLCV.add(tempmax);
				otrval.set(tempmax, Integer.MAX_VALUE);
			}
			return listLCV;
		}

		else {
			return listconsraints.get(v);
		}
	}

	public ArrayList<Integer> nextVals(int v) {
		ArrayList<Integer> numvar = new ArrayList<>();
		for (MapColorConstraints mapcons : constraints) {
			if (mapcons.getConstraintvalues().contains(v)) {
				for (int var : mapcons.getConstraintvalues()) {
					if (!numvar.contains(var) && var != v) {
						numvar.add(var);
					}
				}
			}
		}
		return numvar;
	}

	/*
	 * A function to input CSP of MAP with different components and return false
	 * if not consistent
	 */

	@SuppressWarnings("unchecked")
	public boolean MAC3(int v, ArrayList<Integer> assignvals) {
		LinkedList<Path> path = new LinkedList<>();
		ArrayList<Path> passvals = getNextPath(v, assignvals);
		path.addAll(unUsedVals(passvals, assignvals));

		while (!path.isEmpty()) {
			Path temppath = path.pop();
			ArrayList<ArrayList<Integer>> list = new ArrayList<>();
			for (ArrayList<Integer> arr : listconsraints) {
				list.add((ArrayList<Integer>) arr.clone());
			}
			if (checknextvals(v, temppath.getFirstpath(), assignvals)) {
				if (listconsraints.get(v).size() == 0) {
					listconsraints = list;
					return false;
				}
				for (Path p : passvals) {
					if (!path.contains(p) && p != temppath) {
						path.push(p);
					}
				}
			}
		}
		return true;
	}

	public boolean checknextvals(int v1, int v2, ArrayList<Integer> assignvals) {
		boolean flag = false;
		for (int tempv1 : listconsraints.get(v1)) {
			int temp = assignvals.get(v1);
			assignvals.set(v1, tempv1);
			int count = 0;
			for (int i : listconsraints.get(v2)) {
				int j = assignvals.get(v2);
				assignvals.set(v2, i);
				boolean tempflag = checkConstraints(assignvals);
				assignvals.set(v2, j);
				if (tempflag)
					count++;
			}
			if (count <= 0) {
				listconsraints.get(v1).remove(listconsraints.get(v1).indexOf(tempv1));
				flag = true;
			}
			assignvals.set(v1, temp);
		}
		return flag;
	}

	/*
	 * A function implemented to get the neighbors
	 */
	public ArrayList<Path> getNextPath(int v, ArrayList<Integer> assignval) {
		ArrayList<Path> arr = new ArrayList<>();
		for (Path path : getAllconstrpath()) {
			if (v == path.getLastpath()) {
				arr.add(path);
			}
		}
		return arr;
	}

	public ArrayList<Path> unUsedVals(ArrayList<Path> nextpath, ArrayList<Integer> assignvals) {
		ArrayList<Path> arrpath = new ArrayList<Path>();
		for (Path path : nextpath) {
			if (assignvals.get(path.getFirstpath()) == -1) {
				arrpath.add(path);
			}
		}
		return arrpath;
	}

}
