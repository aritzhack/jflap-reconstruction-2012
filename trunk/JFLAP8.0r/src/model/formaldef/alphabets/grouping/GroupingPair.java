package model.formaldef.alphabets.grouping;

public class GroupingPair {

	
	private String OPEN, CLOSE;

	public GroupingPair(String open, String close){
		OPEN = open;
		CLOSE = close;
	}

	public GroupingPair(char open, char close) {
		this(Character.toString(open), Character.toString(close));
	}

	public GroupingPair() {
		this("", "");
	}

	public String getOpenGroup() {
		return OPEN;
	}

	public String getCloseGroup() {
		return CLOSE;
	}

	public String toString(){
		return "Grouping: " + this.toShortString();
	}
	
	public String toShortString(){
		return this.OPEN + " " + this.CLOSE;
	}
	
	public boolean equals(GroupingPair o){
		return this.toShortString().equals(o.toShortString());
	}

	public boolean isUsable() {
		return !OPEN.isEmpty() && !CLOSE.isEmpty();
	}

	public String creatGroupedString(String string) {
		return this.getOpenGroup() + string + this.getCloseGroup();
	}
	
}
