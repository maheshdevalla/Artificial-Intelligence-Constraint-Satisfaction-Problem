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

class Path {
	private int firstpath, lastpath;

	Path(int firstpath, int lastpath) {
		this.firstpath = firstpath;
		this.lastpath = lastpath;
	}

	public int getFirstpath() {
		return firstpath;
	}

	public void setFirstpath(int firstpath) {
		this.firstpath = firstpath;
	}

	public int getLastpath() {
		return lastpath;
	}

	public void setLastpath(int lastpath) {
		this.lastpath = lastpath;
	}
}
