package view.sets;

import java.lang.reflect.Constructor;

import model.numbersets.defined.MultiplesOfSet;

public class SetConstructionHelper {
	
	private Class myClass;
	private Constructor myConstructor;
	
	public SetConstructionHelper (Class c) {
		myClass = c;
		myConstructor = myClass.getConstructors()[0];
	
	}
	
	
	public void getRequiredParameters () {
		for (Class c : myConstructor.getParameterTypes()) {
			System.out.println(c.getSimpleName());
		}
	}
	
	
	public static void main (String[] args) {
		SetConstructionHelper helper = new SetConstructionHelper(MultiplesOfSet.class);
		helper.getRequiredParameters();
	}
	

}
