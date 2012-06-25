package model.numbersets.function;

import java.util.ArrayList;

public class CompoundFunction extends Function {
	
	private ArrayList<Function> myComponents;
	
	public CompoundFunction() {
		myComponents = new ArrayList<Function>();
	}
	
	public CompoundFunction(Function... funcs) {
		
	}

	@Override
	public int evaluate(int n) {
		int answer = 0;
		for (Function f : myComponents)
			answer += f.evaluate(n);
		return answer;
	}

	@Override
	public boolean canDerive(int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Function f : myComponents.subList(0, myComponents.size() - 1)) 
			sb.append(f.toString() + " + ");
		sb.append(myComponents.get(myComponents.size() - 1));
		return sb.toString();
	}
	
	
	

}
