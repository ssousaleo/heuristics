package priorityheuristics.files;

import java.util.Comparator;

import priorityheuristics.heuristics.DesignProblemElement;

public class DesignProblemComparator implements Comparator<DesignProblemElement> {

	public DesignProblemComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(DesignProblemElement dp1, DesignProblemElement dp2) {
		return (dp1.getDp() - dp2.getDp());
	}

}
