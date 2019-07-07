package priorityheuristics;

import priorityheuristics.files.ReadFiles;
import priorityheuristics.heuristics.Heuristics;
import priorityheuristics.heuristics.Statistic;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadFiles read = new ReadFiles();
		read.parseDesignProblemFiles();
		
		Statistic s = new Statistic(read.getProjects());
		s.runAll();
		
		//Heuristics h = new Heuristics(read.getProjects());
		
		//h.runHeuristicsJsonOutput();
		//h.runHeuristics();
		//h.runHeuristicsRecallJsonOutput();
		//h.runHeuristicsHalfJsonOutput();
	}

}
