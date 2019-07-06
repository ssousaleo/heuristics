package priorityheuristics.heuristics;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class SmellyElement{
	private String path;
	private Integer amount;
	private Integer application;
	private Integer classe;
	private Integer method;
	private HashSet<String> types;

	public SmellyElement() {
		// TODO Auto-generated constructor stub
		types = new HashSet<>();
		amount = 0;
		application = 0;
		classe = 0;
		method = 0;
	}

	public SmellyElement(String path) {
		this();
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public HashSet<String> getTypes() {
		return types;
	}

	public void setTypes(HashSet<String> types) {
		this.types = types;
	}
	
	public void addSmell(String smell, int value) {
		++amount;
		types.add(smell);
		
		if(Smells.APPLICATION == value) {
			++application;
		}else {
			if(Smells.CLASS == value) {
				++classe;
			}else {
				++method;
			}
		}
	}
	
	
	
	public Integer getApplication() {
		return application;
	}

	public void setApplication(Integer application) {
		this.application = application;
	}

	public Integer getClasse() {
		return classe;
	}

	public void setClasse(Integer classe) {
		this.classe = classe;
	}

	public Integer getMethod() {
		return method;
	}

	public void setMethod(Integer method) {
		this.method = method;
	}

	//Implements the Density
	public static Comparator<SmellyElement> densityComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				return (o2.getAmount() - o1.getAmount());
			}
		};
	}
	
	//Implements the Diversity
	public static Comparator<SmellyElement> diversityComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				return (o2.getTypes().size() - o1.getTypes().size());
			}
		};
	}
	
	//Implements the Diversity
	public static Comparator<SmellyElement> applicattionComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				return (o2.application - o1.application);
			}
		};
	}
	
	//Implements the Diversity
	public static Comparator<SmellyElement> classComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				return (o2.classe - o1.classe);
			}
		};
	}
	
	//Implements the Diversity
	public static Comparator<SmellyElement> methodComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				return (o2.method - o1.method);
			}
		};
	}
	
	//Implements the Multiply
	public static Comparator<SmellyElement> multipleComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				// TODO Auto-generated method stub
				int value2 = o2.getTypes().size() * o2.getAmount();
				int value1 = o1.getTypes().size() * o1.getAmount();
				
				return (value2 - value1);
			}
		};
	}
	
	//Implements the Multiply
	public static Comparator<SmellyElement> densityDiversityComparator(){
			return new Comparator<SmellyElement>() {

				@Override
				public int compare(SmellyElement o1, SmellyElement o2) {
					CompareToBuilder builder = new CompareToBuilder();
					
					builder.append(o2.getAmount(), o1.getAmount());
					builder.append(o2.getTypes().size(), o1.getTypes().size());
					builder.append(o2.application, o1.application);
					builder.append(o2.classe, o1.classe);
					builder.append(o2.method, o1.method);
					
					return builder.toComparison();
				}
			};
	}
	
	public static Comparator<SmellyElement> diversityDensityComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				CompareToBuilder builder = new CompareToBuilder();

				builder.append(o2.getTypes().size(), o1.getTypes().size());
				builder.append(o2.getAmount(), o1.getAmount());
				builder.append(o2.application, o1.application);
				builder.append(o2.classe, o1.classe);
				builder.append(o2.method, o1.method);
				
				return builder.toComparison();
			}
		};
	}
	
	
	public static Comparator<SmellyElement> diversityTypeComparator(){
				return new Comparator<SmellyElement>() {

					@Override
					public int compare(SmellyElement o1, SmellyElement o2) {
						CompareToBuilder builder = new CompareToBuilder();
						
						builder.append(o2.getTypes().size(), o1.getTypes().size());
						builder.append(o2.application, o1.application);
						builder.append(o2.classe, o1.classe);
						builder.append(o2.method, o1.method);
						
						return builder.toComparison();
					}
				};
	}
	
	//Implements the Multiply
		public static Comparator<SmellyElement> densityTypeComparator(){
					return new Comparator<SmellyElement>() {

						@Override
						public int compare(SmellyElement o1, SmellyElement o2) {
							CompareToBuilder builder = new CompareToBuilder();
							
							builder.append(o2.getAmount(), o1.getAmount());
							builder.append(o2.application, o1.application);
							builder.append(o2.classe, o1.classe);
							builder.append(o2.method, o1.method);
							
							return builder.toComparison();
						}
					};
		}
		
		public static Comparator<SmellyElement> TypeDiversityComparator(){
			return new Comparator<SmellyElement>() {

				@Override
				public int compare(SmellyElement o1, SmellyElement o2) {
					CompareToBuilder builder = new CompareToBuilder();
					
					builder.append(o2.application, o1.application);
					builder.append(o2.classe, o1.classe);
					builder.append(o2.getTypes().size(), o1.getTypes().size());
					
					return builder.toComparison();
				}
			};
		}
		
		public static Comparator<SmellyElement> typeDensityComparator(){
			return new Comparator<SmellyElement>() {

				@Override
				public int compare(SmellyElement o1, SmellyElement o2) {
					CompareToBuilder builder = new CompareToBuilder();
					
					builder.append(o2.application, o1.application);
					builder.append(o2.classe, o1.classe);
					builder.append(o2.getAmount(), o1.getAmount());
					
					return builder.toComparison();
				}
			};
		}
	
	public static Comparator<SmellyElement> typeComparator(){
		return new Comparator<SmellyElement>() {

			@Override
			public int compare(SmellyElement o1, SmellyElement o2) {
				CompareToBuilder builder = new CompareToBuilder();
				
				builder.append(o2.getTypes().size(), o1.getTypes().size());
				builder.append(o2.application, o1.application);
				builder.append(o2.classe, o1.classe);
				builder.append(o2.method, o1.method);
				
				return builder.toComparison();
			}
		};
	}

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(path + "; Smells: " + amount + "; Types: " + types.size() + "\n");
		
		Iterator<String> i = types.iterator();
		
		while(i.hasNext()) {
			sb.append(i.next() + "\n");
		}
		
		sb.append("\n");
		return sb.toString();
	}


}
