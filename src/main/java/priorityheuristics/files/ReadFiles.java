package priorityheuristics.files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import priorityheuristics.heuristics.DesignProblemElement;
import priorityheuristics.heuristics.Smells;
import priorityheuristics.heuristics.SmellyElement;

public class ReadFiles {
	private final String dpPath = "/Users/leonardo/eclipse-workspace/heuristics/data/dp/";
	private final String smellsPath = "/Users/leonardo/eclipse-workspace/heuristics/data/smells/";
	private List<Project> projects;
	private Smells smellsDefine;

	public ReadFiles() {
		projects = new ArrayList<>();
		smellsDefine = new Smells();
	}
	
	public void parseDesignProblemFiles() {
		File file = new File(dpPath);
		File[] dpFiles;
		
		dpFiles = file.listFiles();
		
		for(File f: dpFiles) {
			if(f.getName().contains("DS_Store")) {
				continue;
			}
			
			Project project = parseDP(f);
			parseSmells(new File(smellsPath + f.getName()), project);
		}
	}
	
	private Project parseDP(File file) {
		Project project = null;
		Hashtable<String, DesignProblemElement> elements = new Hashtable<>();
		CSVReader csvReader = null;
		try {
			FileReader reader = new FileReader(file);
			
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
			
			String[] nextRecord;
			
			while( (nextRecord = csvReader.readNext()) != null) {
				getDesignProblem(nextRecord, elements);
			}
			
			project = new Project();
			project.setName(file.getName());
			project.setDp(getDesignProblems(elements));
			
			projects.add(project);
			
			csvReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return project;
		
	}
	
	private void parseSmells(File file, Project project) {
		Hashtable<String, SmellyElement> elements = new Hashtable<>();
		
		CSVReader csvReader = null;
		try {
			FileReader reader = new FileReader(file);
			
			CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
			csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
			
			String[] nextRecord;
			
			while( (nextRecord = csvReader.readNext()) != null) {
				getSmells(nextRecord, elements, file);
			}
			
			project.setSmelly(new ArrayList<>(elements.values()));
			csvReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void getDesignProblem(String[] nextRecord, Hashtable<String, DesignProblemElement> elements) {
		String type = nextRecord[0];
		
		if(type.equals("Class")) {
			//verify if the element is anonymous class
			if(nextRecord[1].contains(".(Anon_")) {
				nextRecord[1] = getClass(nextRecord[1]);
			}
			if(nextRecord[1] != null) {
				computeElement(nextRecord, elements);
			}
			
		}else{
			
			if(type.contains("Method") || type.contains("TypeVariable") || type.contains("Constructor") ) {
				try {
					nextRecord[1] = getClass(nextRecord[1]);
					computeElement(nextRecord, elements);
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					return;
				} 
				
			}
		}
	}

	private void computeElement(String[] nextRecord, Hashtable<String, DesignProblemElement> elements) {
		if(elements.containsKey(nextRecord[1])) {
			//get dp
			elements.get(nextRecord[1]).adDp(countDP(nextRecord));
		}else {
			
			//add element
			DesignProblemElement element = new DesignProblemElement();
			
			element.setPath(nextRecord[1]);
			element.setDp(countDP(nextRecord));
			
			elements.put(nextRecord[1], element);
		}
	}
	
	private String getClass(String string) {
		StringBuffer buffer = new StringBuffer();
		String[] splits = string.split("\\.");
		
		for(String s: splits) {
			buffer.append(s);
			if(Character.isUpperCase(s.charAt(0))) {
				return buffer.toString();
			}
			buffer.append(".");
		}
		
		
		return null;
	}

	private int countDP(String[] nextRecord) {
		int count = 0;
		
		for(int i = 2; i < nextRecord.length; i++) {
			if(!nextRecord[i].equals("")) {
				try {
					count += Integer.parseInt(nextRecord[i]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					continue;
				}
			}
		}
		return count;
	}
	
	public List<DesignProblemElement> getDesignProblems(Hashtable<String, DesignProblemElement> elements) {
		List<DesignProblemElement> list = new ArrayList<>(elements.values());
		
		//remove the elements without dp
		Collections.sort(list);
		
		Iterator<DesignProblemElement> itr = list.iterator();
		DesignProblemElement ele;
		
		while(itr.hasNext()) {
			ele = itr.next();
			
			if(ele.getDp() == 0) {
				itr.remove();
			}
		}
		
		return list;
	}
	
	/*private List<DesignProblemElement> getTopTenDesignProblems(Hashtable<String, DesignProblemElement> elements) {
		List<DesignProblemElement> list = getDesignProblems(elements);
		
		
		
		/*if(list.size() < Heuristics.TOPELEMENTS) {
			return list;
		}*
		
		int i = (int) (list.size() * 0.1);
		boolean ok = true;
		
		while (ok) {
			if(list.get(i-1).getDp() == list.get(i).getDp()) {
				++i;
			}else {
				ok = false;
			}
		}
		
		return list.subList(0, i);
		
	}*/
	
	
	public List<Project> getProjects(){
		return projects;
	}
	
	private void getSmells(String[] nextRecord, Hashtable<String, SmellyElement> elements, File file) {
		String ele = nextRecord[0];
		
		ele = getClass(ele);
		
		if(ele.contains("util")) {
			return;
		}
		
		
		if(elements.containsKey(ele)) {
			//get smells
			countSmells(nextRecord, elements.get(ele));
		}else {
			
			//add element
			SmellyElement smellyElement = new SmellyElement();

			smellyElement.setPath(ele);
			countSmells(nextRecord, smellyElement);
			
			elements.put(ele, smellyElement);
		}
		

	}
	
	private void countSmells(String[] nextRecord, SmellyElement se) {
		
		for(int i = 1; i < nextRecord.length; i++) {
			if(!nextRecord[i].equals("") &&
					!nextRecord[i].contains("Intra") &&
					!nextRecord[i].contains("Hereditary") &&
					!nextRecord[i].contains("Multiple") &&
					!nextRecord[i].contains("Misplaced") &&
					!nextRecord[i].contains("External") &&
					!nextRecord[i].contains("Similar") &&
					!nextRecord[i].contains("Replicated") &&
					!nextRecord[i].contains("Inheritance") &&
					!nextRecord[i].contains("Mutant") &&
					!nextRecord[i].contains("Concern")) {
				se.addSmell(nextRecord[i], smellsDefine.getType(nextRecord[1]));
			}
		}
	}

}
