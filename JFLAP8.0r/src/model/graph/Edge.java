package model.graph;

public class Edge implements Comparable<Edge>{

	
	private Vertex myFrom;
	private Vertex myTo;
	private String myLabel;

	public Edge(Vertex from, Vertex to, String label){
		myFrom = from;
		myTo = to;
		myLabel = label;
	}
	
	
	public Edge(Vertex from, Vertex to) {
		this(from, to, "");
	}


	public Vertex getFromVertex(){
		return myFrom;
	}
	
	public Vertex getToVertex(){
		return myTo;
	}
	
	public String getLabel(){
		return myLabel;
	}


	@Override
	public int compareTo(Edge o) {
		
		int compare = this.getFromVertex().compareTo(o.getFromVertex());
		if (compare != 0) return compare;
		
		compare = this.getToVertex().compareTo(o.getToVertex());
		if (compare != 0) return compare;
		
		return this.getLabel().compareTo(o.getLabel());
	}
	
	@Override
	public String toString() {
		return this.getFromVertex().getName() + "---" + 
				this.getLabel() + "--->" + 
					this.getToVertex().getName();
	}
	
}
