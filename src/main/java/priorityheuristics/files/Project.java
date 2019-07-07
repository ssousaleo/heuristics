package priorityheuristics.files;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import priorityheuristics.heuristics.DesignProblemElement;
import priorityheuristics.heuristics.Heuristics;
import priorityheuristics.heuristics.SmellyElement;

public class Project {
	private String name;
	private List<DesignProblemElement> dp;
	private List<SmellyElement> smelly;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DesignProblemElement> getDp() {
		return dp;
	}
	public void setDp(List<DesignProblemElement> dp) {
		this.dp = dp;
	}
	
	
	public List<SmellyElement> getSmelly() {
		return smelly;
	}
	
	public void setSmelly(List<SmellyElement> smelly) {
		this.smelly = smelly;
	}
	
	public List<String> getElementsDP(){
		List<String> elements = new ArrayList<>();
		
		for(DesignProblemElement dpe: dp) {
			elements.add(dpe.getPath());
		}
		
		return elements;
	}
	
	public Hashtable<String, Integer> getDesignProblemElementsCount(){
		Hashtable<String, Integer> hash = new Hashtable<>(dp.size());
		
		for(DesignProblemElement dpe: dp) {
			hash.put(dpe.getPath(), dpe.getDp());
		}
		
		
		return hash;
	}
	
	public List<String> getTopPercentElementsDP(){
		List<String> elements = new ArrayList<>();
		int limit = (dp.size() * Heuristics.TOPELEMENTS)/100;
		
		
		boolean ok = true;
		
		if(limit == 0) {
			limit = 1;
		}
		
		if(limit < dp.size()) {
		
			while(ok) {
				if(dp.get(limit-1).getDp() == dp.get(limit).getDp()) {
					++limit;
				}else {
					ok = false;
				}
			}
		}
		for(int i = 0; i < limit; i++) {
			elements.add(dp.get(i).getPath());
		}
		
		return elements;
	}
	
	public List<String> getTopDesignProblemElements(){
		List<String> elements = new ArrayList<>();
		int limit = Heuristics.TOPELEMENTS;
		
		
		if(limit == 0) {
			limit = 1;
		}
		
		
		for(int i = 0; i < limit; i++) {
			elements.add(dp.get(i).getPath());
		}
		
		return elements;
	}
	
	public List<String> getAllDesignProblemElements(){
		List<String> elements = new ArrayList<>();
		
		for(DesignProblemElement dpe: dp) {
			elements.add(dpe.getPath());
		}
		
		return elements;
	}
	
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("Project: " + name + "\nDesign problems:\n");
		for(DesignProblemElement d: dp) {
			b.append(d.toString() + "\n");
		}
		
		return b.toString();
		
	}

}
