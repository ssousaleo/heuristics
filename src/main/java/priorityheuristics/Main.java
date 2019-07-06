package priorityheuristics;

import priorityheuristics.files.ReadFiles;
import priorityheuristics.heuristics.Heuristics;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadFiles read = new ReadFiles();
		read.parseDesignProblemFiles();
		
		Heuristics h = new Heuristics(read.getProjects());
		//h.runHeuristics();
		h.runHeuristicsJsonOutput();
		//h.runHeuristicsRecallJsonOutput();

		//h.runHeuristicsHalfJsonOutput();
	}

}
