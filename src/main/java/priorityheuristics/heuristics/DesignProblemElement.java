package priorityheuristics.heuristics;

public class DesignProblemElement implements Comparable<DesignProblemElement>{
	private String path;
	private Integer dp;

	public DesignProblemElement() {
		// TODO Auto-generated constructor stub
	}

	public DesignProblemElement(String path, String name) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getDp() {
		return dp;
	}

	public void setDp(Integer dp) {
		this.dp = dp;
	}
	
	public void adDp(int dp) {
		this.dp += dp;
	}

	@Override
	public String toString() {
		return "path=" + path + ", dp=" + dp;
	}


	@Override
	public int compareTo(DesignProblemElement o) {
		// TODO Auto-generated method stub
		return (o.getDp() - this.getDp());
	}
	
	

}
