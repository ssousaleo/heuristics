package priorityheuristics.heuristics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Smells {
	private List<String> application;
	private List<String> classe;
	private List<String> method;
	
	public static int APPLICATION = 0;
	public static int CLASS = 1;
	public static int METHOD = 2;

	public Smells() {
		String[]  applicationSmells = {"shotgunsurgery", "duplicateddode"};
		String[] classSmells = {"brainclass", "classdatashouldbeprivate", "complexclass", "dataclass", "godclass", "godclass1", "godclass2", "godclass3", 
				"lazyclass","refusedbequest", "spaghetticode", "speculativegenerality", "middleman", "divergentchange", "dataclumps"};

		String[] methodSmells = {"brainmethod", "longmethod", "longmethod1", "longmethod2", "longmethod3", "longparameterlist", "messagechain","dispersedcoupling",
				"intensivecoupling","featureenvy", "orderedmetricvalues"};
		
		application = new ArrayList<>(Arrays.asList(applicationSmells));
		classe = new ArrayList<>(Arrays.asList(classSmells));
		method = new ArrayList<>(Arrays.asList(methodSmells));
	}
	
	public int getType(String smell) {
		smell = smell.toLowerCase();
		if(application.contains(smell)) {
			return APPLICATION;
		}else {
			if(classe.contains(smell)) {
				return CLASS;
			}else {
				return METHOD;
			}
		}
	}

}
