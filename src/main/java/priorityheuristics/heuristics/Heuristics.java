package priorityheuristics.heuristics;

import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import priorityheuristics.files.Project;

public class Heuristics {
	public static int TOPELEMENTS = 20;
			
	public static boolean ALLDP = true;
	private List<Project> projects;
	
	public Heuristics(List<Project> projects) {
		this.projects = projects;
	}
	
	public void runHeuristicsHalfJsonOutput() {
		StringBuffer buffer = new StringBuffer();
		DecimalFormat format = new DecimalFormat("0.00");
		
		int[] pHits = {0, 0};
		int[] total = {0, 0};
		
		List<Object> values;
		Double precision;
		
		for(Project p: projects) {
			
			//buffer.append(p.getName() + ",");
			
			//Heuristic 1: density
			values = runHeuristicHalf(p, SmellyElement.densityComparator(), SmellyElement.diversityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[0] += (int) values.get(1);
			total[0] += (int) values.get(2);
			
			//Heuristic 2: diversity
			values = runHeuristicHalf(p, SmellyElement.diversityComparator(), SmellyElement.densityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%");
			pHits[1] += (int) values.get(1);
			total[1] += (int) values.get(2);
			
			
			buffer.append("\n");
			
		}
		
		//buffer.append("Projects Overrall,");

		precision = (double) pHits[0] / (double) total[0];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		
		precision = (double) pHits[1] / (double) total[1];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		


		System.out.println(buffer.toString());
		
	}
	
	public void runHeuristicsJsonOutput() {
		StringBuffer buffer = new StringBuffer();
		DecimalFormat format = new DecimalFormat("0.00");
		
		int[] pHits = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] total = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		
		List<Object> values;
		Double precision;
		
		buffer.append("Project,Granularity,Quantity,Variety,Quant/Var,Var/Quant,Quant/Gran,Var/Gran,Gran/Quant,Gran/Var\n");
		
		for(Project p: projects) {
			
			buffer.append(p.getName() + ",");
			
			//Smell Granularity
			values = runHeuristic(p, SmellyElement.typeComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[0] += (int) values.get(1);
			total[0] += (int) values.get(2);
			
			//Quantity
			values = runHeuristic(p, SmellyElement.densityComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[1] += (int) values.get(1);
			total[1] += (int) values.get(2);
			
			//Variety
			values = runHeuristic(p, SmellyElement.diversityComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[2] += (int) values.get(1);
			total[2] += (int) values.get(2);
			
			
			//Quant/Var
			values = runCombinedHeuristicsTieVar(p, SmellyElement.densityComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[3] += (int) values.get(1);
			total[3] += (int) values.get(2);
			
			//Var/Quant
			values = runCombinedHeuristicsTieDen(p, SmellyElement.diversityComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[4] += (int) values.get(1);
			total[4] += (int) values.get(2);
			
			
			//Quant/Gran
			values = runCombinedHeuristicsTieType(p, SmellyElement.densityComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[5] += (int) values.get(1);
			total[5] += (int) values.get(2);
			
			//Var/Gran
			values = runCombinedHeuristicsTieType(p, SmellyElement.diversityComparator());
			values = runHeuristic(p, SmellyElement.diversityTypeComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[6] += (int) values.get(1);
			total[6] += (int) values.get(2);
			
			//Gran/Quant
			values = runCombinedHeuristicsTieDen(p, SmellyElement.typeComparator());
			values = runHeuristic(p, SmellyElement.diversityTypeComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%),");
			pHits[7] += (int) values.get(1);
			total[7] += (int) values.get(2);
			
			//Gran/Var
			values = runCombinedHeuristicsTieVar(p, SmellyElement.typeComparator());
			values = runHeuristic(p, SmellyElement.diversityTypeComparator());
			buffer.append((int) values.get(1) + "/" + (int) values.get(2) + " ("+ format.format((Double) values.get(0)) + "%)");
			pHits[8] += (int) values.get(1);
			total[8] += (int) values.get(2);
			
			
			buffer.append("\n");
			
		}
		
		buffer.append("TOP" + TOPELEMENTS + ",");

		precision = (double) pHits[0] / (double) total[0];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		
		precision = (double) pHits[1] / (double) total[1];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		

		precision = (double) pHits[2] / (double) total[2];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");


		precision = (double) pHits[3] / (double) total[3];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");


		precision = (double) pHits[4] / (double) total[4];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		precision = (double) pHits[5] / (double) total[5];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		precision = (double) pHits[6] / (double) total[6];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		precision = (double) pHits[7] / (double) total[7];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		precision = (double) pHits[8] / (double) total[8];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%");



		System.out.println(buffer.toString());
		
	}
	/*
	 * Compara top x com top x
	 */
	public void runHeuristicsRecallJsonOutput() {
		StringBuffer buffer = new StringBuffer();
		DecimalFormat format = new DecimalFormat("0.00");
		
		int[] pHits = {0, 0, 0, 0, 0, 0, 0, 0};
		int[] total = {0, 0, 0, 0, 0, 0, 0, 0};
		
		List<Object> values;
		Double precision;
		
		for(Project p: projects) {
			
			buffer.append(p.getName() + ",");
			
			//Heuristic 1: density
			values = runHeuristicRecall(p, SmellyElement.densityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[0] += (int) values.get(1);
			total[0] += (int) values.get(2);
			
			//Heuristic 2: diversity
			values = runHeuristicRecall(p, SmellyElement.diversityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[1] += (int) values.get(1);
			total[1] += (int) values.get(2);
			
			//Heuristic 3: density/diversity
			values = runHeuristicRecall(p, SmellyElement.densityDiversityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[2] += (int) values.get(1);
			total[2] += (int) values.get(2);
			
			//Heuristic 4: Diversity/Density
			values = runHeuristicRecall(p, SmellyElement.diversityDensityComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[3] += (int) values.get(1);
			total[3] += (int) values.get(2);
			
			//Heuristic 5: Diversity/Type
			values = runHeuristicRecall(p, SmellyElement.diversityTypeComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[4] += (int) values.get(1);
			total[4] += (int) values.get(2);
			
			//Heuristic 6: Type
			values = runHeuristicRecall(p, SmellyElement.typeComparator());
			buffer.append(format.format((Double) values.get(0)) + "%,");
			pHits[5] += (int) values.get(1);;
			total[5] += (int) values.get(2);
	
			
			buffer.append("\n");
			
		}
		
		buffer.append("Projects Overrall,");

		precision = (double) pHits[0] / (double) total[0];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		
		precision = (double) pHits[1] / (double) total[1];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		

		precision = (double) pHits[2] / (double) total[2];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");


		precision = (double) pHits[3] / (double) total[3];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");


		precision = (double) pHits[4] / (double) total[4];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%,");
		
		precision = (double) pHits[5] / (double) total[5];
		precision *= 100.00;
		buffer.append(format.format(precision) + "%");



		System.out.println(buffer.toString());
		
	}
	

	public void runHeuristics() {
		StringBuffer buffer = new StringBuffer();
		DecimalFormat format = new DecimalFormat("0.00");
		
		int[] pHits = {0, 0, 0, 0, 0, 0, 0, 0};
		int[] total = {0, 0, 0, 0, 0, 0, 0, 0};
		
		List<Object> values;
		Double precision;
		
		for(Project p: projects) {
			
			buffer.append("Project: " + p.getName() + " DP " + p.getDp().size() + "\n");
			
			//Heuristic 1: density
			buffer.append("Heuristic: Density");
			values = runHeuristic(p, SmellyElement.densityComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[0] += (int) values.get(1);
			total[0] += (int) values.get(2);
			
			//Heuristic 2: diversity
			buffer.append("Heuristic: Diversity");
			values = runHeuristic(p, SmellyElement.diversityComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[1] += (int) values.get(1);
			total[1] += (int) values.get(2);
			
			//Heuristic 3: density/diverstiy
			buffer.append("Heuristic: Density/Diversity");
			values = runCombinedHeuristics(p, SmellyElement.densityComparator(), SmellyElement.densityDiversityComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[2] += (int) values.get(1);
			total[2] += (int) values.get(2);
			
			//Heuristic 4: multiply
			buffer.append("Heuristic: Diversity/Density");
			values = runCombinedHeuristics(p, SmellyElement.diversityComparator(), SmellyElement.diversityDensityComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[3] += (int) values.get(1);
			total[3] += (int) values.get(2);
			
			//Heuristic 5: multiply
			buffer.append("Heuristic: Multiply");
			values = runHeuristic(p, SmellyElement.multipleComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[4] += (int) values.get(1);
			total[4] += (int) values.get(2);
			
			//Heuristic 6: Application
			buffer.append("Heuristic: Application");
			values = runHeuristic(p, SmellyElement.applicattionComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[5] += (int) values.get(1);;
			total[5] += (int) values.get(2);
			
			//Heuristic 7: Class
			buffer.append("Heuristic: Class");
			values = runHeuristic(p, SmellyElement.classComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[6] += (int) values.get(1);
			total[6] += (int) values.get(2);
			
			//Heuristic 8: Method
			buffer.append("Heuristic: Method");
			values = runHeuristic(p, SmellyElement.methodComparator());
			buffer.append("; Precision: " + format.format((Double) values.get(0)) + "% (" + values.get(1) + ")\n");
			pHits[7] += (int) values.get(1);
			total[7] += (int) values.get(2);
			
			buffer.append("\n\n");
			
		}
		
		buffer.append("Projects Overrall");
		
		
		buffer.append("\nHeuristic: Density");
		precision = (double) pHits[0] / (double) total[0];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[0] + ")\n");
		
		
		buffer.append("Heuristic: Diversity");
		precision = (double) pHits[1] / (double) total[1];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[1] + ")\n");
		
		buffer.append("Heuristic: Density/Diversity");
		precision = (double) pHits[2] / (double) total[3];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[2] + ")\n");
		
		buffer.append("Heuristic: Diversity/Density");
		precision = (double) pHits[3] / (double) total[3];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[3] + ")\n");
		
		buffer.append("Heuristic: Multiply");
		precision = (double) pHits[4] / (double) total[4];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[4] + ")\n");
		
		buffer.append("Heuristic: Application");
		precision = (double) pHits[5] / (double) total[5];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[4] + ")\n");
		
		buffer.append("Heuristic: Class");
		precision = (double) pHits[6] / (double) total[6];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[4] + ")\n");
		
		buffer.append("Heuristic: Method");
		precision = (double) pHits[7] / (double) total[7];
		precision *= 100.00;
		buffer.append("; Precision: " + format.format(precision) + "% (" + pHits[4] + ")\n");
		
		System.out.println(buffer.toString());
		
	}

	private List<Object> runHeuristic(Project p, Comparator<SmellyElement> comparator) {
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
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
				
		List<String> smells = getPaths(list.subList(0, i));
		
		int tp = 0;
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++tp;
			}
		}
		

		Double precision = (double) tp / (double)i;
		precision *= 100.00;

		values.add(precision);
		values.add(new Integer(tp));
		values.add(new Integer(i));
		
		return values;
		
	}
	
	private List<Object> runHeuristicHalf(Project p, Comparator<SmellyElement> first, Comparator<SmellyElement> second) {
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
		//pega os smells e os ordena
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, first);
		
		
		List<SmellyElement> half = list.subList(0, list.size()/2);
		
		Collections.sort(half, second);
		
		int i = TOPELEMENTS;
		List<String> smells;
		
		if(i > half.size()) {
			smells = getPaths(half);
		}else {
			smells = getPaths(half.subList(0, i));
		}
				
		
		int tp = 0;
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++tp;
			}
		}
		

		Double precision = (double) tp / (double)i;
		precision *= 100.00;

		values.add(precision);
		values.add(new Integer(tp));
		values.add(new Integer(i));
		
		return values;
		
	}
	
	private List<Object> runHeuristicRecall(Project p, Comparator<SmellyElement> comparator) {
		List<Object> values = new ArrayList<>();
		List<String> dps = p.getAllDesignProblemElements(); //oraculo
		
		List<SmellyElement> list = p.getSmelly();
		
		Collections.sort(list, comparator);
		
		
		//tamanho do oraculo
		int i = dps.size();
		
		List<String> smells = getPaths(list.subList(0, i));
		
		int tp = 0;
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++tp;
			}
		}
		

		Double recall = (double) tp / (double)i;
		recall *= 100.00;

		values.add(recall);
		values.add(new Integer(tp));
		values.add(new Integer(i));
		
		return values;
		
	}
	
	
	private List<Object> runCombinedHeuristicsTieType(Project p, Comparator<SmellyElement> first){
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
		List<SmellyElement> list = p.getSmelly();
		
		
		//apply the first heuristic
		Collections.sort(list, first);
		
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
		
		List<String> smells = getPaths(fArray);
				
		int hits = 0;
		
		//reordena a sublista com o segundo comparator
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++hits;
			}
		}
		
		Double precision = (double) hits / (double)i;
		precision *= 100.00;
		

		values.add(precision);
		values.add(new Integer(hits));
		values.add(new Integer(i));
		
		return values;
	}
	
	private List<Object> runCombinedHeuristicsTieVar(Project p, Comparator<SmellyElement> first){
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
		List<SmellyElement> list = p.getSmelly();
		
		
		//apply the first heuristic
		Collections.sort(list, first);
		
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
				
				
				/*if(subList.get(j).getApplication() == last.getApplication()) {
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
				}*/
			}
		}
		
		List<String> smells = getPaths(fArray);
				
		int hits = 0;
		
		//reordena a sublista com o segundo comparator
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++hits;
			}
		}
		
		Double precision = (double) hits / (double)i;
		precision *= 100.00;
		

		values.add(precision);
		values.add(new Integer(hits));
		values.add(new Integer(i));
		
		return values;
	}
	
	private List<Object> runCombinedHeuristicsTieDen(Project p, Comparator<SmellyElement> first){
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
		List<SmellyElement> list = p.getSmelly();
		
		
		//apply the first heuristic
		Collections.sort(list, first);
		
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
				
				
				/*if(subList.get(j).getApplication() == last.getApplication()) {
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
				}*/
			}
		}
		
		List<String> smells = getPaths(fArray);
				
		int hits = 0;
		
		//reordena a sublista com o segundo comparator
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++hits;
			}
		}
		
		Double precision = (double) hits / (double)i;
		precision *= 100.00;
		

		values.add(precision);
		values.add(new Integer(hits));
		values.add(new Integer(i));
		
		return values;
	}
	
	private List<Object>  runCombinedHeuristics(Project p, Comparator<SmellyElement> first, Comparator<SmellyElement> second) {
		
		List<Object> values = new ArrayList<>();
		List<String> dps;
		
		//Get Top  10% or all elements with DP
		if(ALLDP) {
			dps = p.getElementsDP();
		}else {
			dps = p.getTopPercentElementsDP();
		}
		
		List<SmellyElement> list = p.getSmelly();
		
		
		//apply the first heuristic
		Collections.sort(list, first);
		
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
		
		//apply the second heuristic
		Collections.sort(subList, second);
		
		ok = true;
		i = TOPELEMENTS;
		
		while (ok && i < subList.size()) {
			if(subList.get(i-1).getAmount() == subList.get(i).getAmount()) {
				++i;
			}else {
				ok = false;
			}
		}
				
		List<String> smells = getPaths(subList.subList(0, i));
		
		int hits = 0;
		
		//reordena a sublista com o segundo comparator
		
		for(String s: smells) {
			if(dps.contains(s)) {
				++hits;
			}
		}
		
		Double precision = (double) hits / (double)i;
		precision *= 100.00;
		

		values.add(precision);
		values.add(new Integer(hits));
		values.add(new Integer(i));
		
		return values;
		
	}
	
	
	private List<String> getPaths(List<SmellyElement> sElements){
		List<String> smells = new ArrayList<>();
		
		for(SmellyElement s: sElements) {
			smells.add(s.getPath());
		}
		
		return smells;
	}

}
