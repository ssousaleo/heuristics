package priorityheuristics.heuristics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;


import priorityheuristics.files.Project;

public class Statistic {
	public StringBuffer buffer = new StringBuffer();
	public static int TOPELEMENTS = 20;
	
	public static boolean ALLDP = true;
	private List<Project> projects;
	
	public Statistic(List<Project> projects) {
		this.projects = projects;
	}
	
	public void runAll() {
		for(Project p: projects) {
			List<DesignProblemElement> dp =  p.getDp();
			buffer.append(p.getName() + "\n");
			
			
			//Smell Granularity
			buffer.append("\n\nGranularity" + "\n");
			buffer.append("rank;element;quant dps\n");
			rankAllElements(p, SmellyElement.typeComparator());
			
			//Smell Quantity
			buffer.append("\n\nDensity" + "\n");
			buffer.append("rank;element;quant dps\n");
			rankAllElements(p, SmellyElement.densityComparator());
			
			
			//Variety
			buffer.append("\n\nDiversity" + "\n");
			buffer.append("rank;element;quant dps\n");
			rankAllElements(p, SmellyElement.diversityComparator());
			
			
			buffer.append("\n\n\n\n");
		}
		
		try {
			FileWriter writer = new FileWriter(new File("/Users/leonardo/eclipse-workspace/heuristics/data/allelements.txt"));
			PrintWriter printWriter = new PrintWriter(writer);
			printWriter.print(buffer.toString());
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		for(Project p: projects) {
			List<DesignProblemElement> dp =  p.getDp();
			System.out.println(p.getName() + "\n");
			
			
			//Smell Granularity
			System.out.println("\n\nGranularity" + "\n");
			rankElements(p, SmellyElement.typeComparator());
			
			//Smell Quantity
			System.out.println("\n\nDensity" + "\n");
			rankElements(p, SmellyElement.densityComparator());
			
			
			//Variety
			System.out.println("\n\nDiversity" + "\n");
			rankElements(p, SmellyElement.diversityComparator());
			
			//Quant/Var
			System.out.println("\n\nDen & Div" + "\n");
			runCombinedHeuristicsTieVar(p, SmellyElement.densityComparator());
			
			//Var/Quant
			System.out.println("\n\nDiv & Den" + "\n");
			runCombinedHeuristicsTieDen(p, SmellyElement.diversityComparator());
			
			//Quant/Gran
			System.out.println("\n\nDen & Gran" + "\n");
			runCombinedHeuristicsTieType(p, SmellyElement.densityComparator());
			
			//Var/Gran
			System.out.println("\n\nDiv & Gran" + "\n");
			runCombinedHeuristicsTieType(p, SmellyElement.diversityComparator());
			
			//Gran/Quant
			System.out.println("\n\nGran & Den" + "\n");
			runCombinedHeuristicsTieDen(p, SmellyElement.typeComparator());
			
			//Gran/Var
			System.out.println("\n\nGran & Div" + "\n");
			runCombinedHeuristicsTieVar(p, SmellyElement.typeComparator());
			
			System.out.println("\n\n\n\n\n\n");
		}
	}
	
	public void rankAllElements(Project p, Comparator<SmellyElement> comparator) {
		Hashtable<String, Integer> hash = p.getDesignProblemElementsCount();
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
	
		//List<String> smells = getPaths(list.subList(0, i));
		int rank = 0;
		for (SmellyElement el: list) {
			buffer.append(++rank + ";" + el.getPath() + ";" + getDPValue(el.getPath(), hash) + "\n");
		}
		
	}
	
	public void rankElements(Project p, Comparator<SmellyElement> comparator) {
		Hashtable<String, Integer> hash = p.getDesignProblemElementsCount();
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
		
		int i = TOPELEMENTS;
		
		//get Top elements
		if(list.size() > TOPELEMENTS) {
			boolean ok = true;
			
			while (ok) {
				if(list.get(i-1).getAmount() == list.get(i).getAmount()) {
					++i;
				}else {
					ok = false;
				}
			}
		}
		
		if(i >= list.size()) {
			i = list.size();
		}
				
		//List<String> smells = getPaths(list.subList(0, i));
		int rank = 0;
		for (SmellyElement el: list.subList(0, i)) {
			System.out.println(++rank + ";" + el.getPath() + ";" + getDPValue(el.getPath(), hash));
		}
		
	}
	
	public void runCombinedHeuristicsTieVar(Project p, Comparator<SmellyElement> comparator) {
		Hashtable<String, Integer> hash = p.getDesignProblemElementsCount();
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
		
		//define limit
		int limit = TOPELEMENTS * 2;
		
		if(limit > list.size() -1) {
			limit = list.size() -1;
		}
		
		//percorre a lista total pra gerar a sublista
		boolean ok = true;
		int i = limit;
		
		while (ok) {
			if(list.get(i-1).getAmount() == list.get(i).getAmount()) {
				++i;
			}else {
				ok = false;
			}
		}
		
		if(i > (list.size() -1)) {
			i = list.size() -1;
		}
		
		List<SmellyElement> subList = list.subList(0, i);
		
		int j = 0;
		
		List<SmellyElement> fArray = new ArrayList<>();
		
		SmellyElement last = null;
		
		for(j = 0; j < i; j++) {
			if (j < TOPELEMENTS) {
				fArray.add(subList.get(j));
				last = subList.get(j);
			}else {
				if(subList.get(j).getTypes().size() == last.getTypes().size()) {
					fArray.add(subList.get(j));
					last = subList.get(j);
				}else {
					if(subList.get(j).getTypes().size() > last.getTypes().size()) {
						fArray.remove(last);
						fArray.add(subList.get(j)); 
					}
					break;
				}
			}
		}
				
		//List<String> smells = getPaths(list.subList(0, i));
		int rank = 0;
		for (SmellyElement el: fArray) {
			System.out.println(++rank + ";" + el.getPath() + ";" + getDPValue(el.getPath(), hash));
		}
		
	}
	
	public void runCombinedHeuristicsTieDen(Project p, Comparator<SmellyElement> comparator) {
		Hashtable<String, Integer> hash = p.getDesignProblemElementsCount();
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
		
		//define limit
		int limit = TOPELEMENTS * 2;
		
		if(limit > list.size() -1) {
			limit = list.size() -1;
		}
		
		//percorre a lista total pra gerar a sublista
		boolean ok = true;
		int i = limit;
		
		while (ok) {
			if(list.get(i-1).getAmount() == list.get(i).getAmount()) {
				++i;
			}else {
				ok = false;
			}
		}
		
		if(i > (list.size() -1)) {
			i = list.size() -1;
		}
		
		List<SmellyElement> subList = list.subList(0, i);
		
		int j = 0;
		
		List<SmellyElement> fArray = new ArrayList<>();
		
		SmellyElement last = null;
		
		for(j = 0; j < i; j++) {
			if (j < TOPELEMENTS) {
				fArray.add(subList.get(j));
				last = subList.get(j);
			}else {
				if(subList.get(j).getAmount() == last.getAmount()) {
					fArray.add(subList.get(j));
					last = subList.get(j);
				}else {
					if(subList.get(j).getAmount() > last.getAmount()) {
						fArray.remove(last);
						fArray.add(subList.get(j)); 
					}
					break;
				}
			}
		}
				
		//List<String> smells = getPaths(list.subList(0, i));
		int rank = 0;
		for (SmellyElement el: fArray) {
			System.out.println(++rank + ";" + el.getPath() + ";" + getDPValue(el.getPath(), hash));
		}
		
	}
	
	public void runCombinedHeuristicsTieType(Project p, Comparator<SmellyElement> comparator) {
		Hashtable<String, Integer> hash = p.getDesignProblemElementsCount();
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
		
		//define limit
		int limit = TOPELEMENTS * 2;
		
		if(limit > list.size() -1) {
			limit = list.size() -1;
		}
		
		//percorre a lista total pra gerar a sublista
		boolean ok = true;
		int i = limit;
		
		while (ok) {
			if(list.get(i-1).getAmount() == list.get(i).getAmount()) {
				++i;
			}else {
				ok = false;
			}
		}
		
		if(i > (list.size() -1)) {
			i = list.size() -1;
		}
		
		List<SmellyElement> subList = list.subList(0, i);
		
		int j = 0;
		
		List<SmellyElement> fArray = new ArrayList<>();
		
		SmellyElement last = null;
		
		for(j = 0; j < i; j++) {
			if (j < TOPELEMENTS) {
				fArray.add(subList.get(j));
				last = subList.get(j);
			}else {
				if(subList.get(j).getApplication() == last.getApplication()) {
					if(subList.get(j).getClasse() == last.getClasse()) {
						if(subList.get(j).getMethod() == last.getMethod()) {
							fArray.add(subList.get(j));
							last = subList.get(j);
						}
					}else {
						if(subList.get(j).getClasse() > last.getClasse()) {
							fArray.add(subList.get(j));
							break;
						}else {
							break;
						}
					}
				}else {
					if(subList.get(j).getApplication() > last.getApplication()) {
						fArray.add(subList.get(j));
						break;
					}else {
						break;
					}
				}
			}
		}
				
		//List<String> smells = getPaths(list.subList(0, i));
		int rank = 0;
		for (SmellyElement el: fArray) {
			System.out.println(++rank + ";" + el.getPath() + ";" + getDPValue(el.getPath(), hash));
		}
		
	}
	
	
	private Integer getDPValue(String elementName, Hashtable<String, Integer> hash) {
		if(hash.containsKey(elementName)) {
			return hash.get(elementName);
		}else {
			return 0;
		}
	}
	
	private List<String> getPaths(List<SmellyElement> sElements){
		List<String> smells = new ArrayList<>();
		
		for(SmellyElement s: sElements) {
			smells.add(s.getPath());
		}
		
		return smells;
	}

}
