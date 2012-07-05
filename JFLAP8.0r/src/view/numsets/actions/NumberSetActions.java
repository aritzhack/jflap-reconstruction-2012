package view.numsets.actions;

import java.util.HashMap;

import model.numbersets.operations.Intersection;
import model.numbersets.operations.Union;

public class NumberSetActions {
	
	private static HashMap<String, Class> operationMap;

	static {
		operationMap = new HashMap<String, Class>();
		operationMap.put("Union", Union.class);
		operationMap.put("Intersection", Intersection.class);
	}

}
