package model.automata;

import java.lang.reflect.Constructor;

import util.Copyable;


import model.JFLAPConstants;
import model.formaldef.components.SetSubComponent;



public class State extends SetSubComponent<State> implements JFLAPConstants, Comparable<State>{

	private String myLabel;

	private int myID;

	private String myName;

	public State(String name, int id){
		setName(name);
		setID(id);
	}



	/**
	 * Sets the name for this state. A parameter of <CODE>null</CODE> will
	 * reset this to the default.
	 * 
	 * @param name
	 *            the new name for the state
	 */
	public void setName(String name) {
		myName = name;
	}

	/**
	 * Returns the simple "name" for this state.
	 * 
	 * @return the name for this state
	 */
	public String getName() {
		return myName;
	}


	public int getID(){
		return myID;
	}
	
	public void setID(int id){
		myID = id;
	}

	@Override 
	public State copy(){
		try{
			Constructor c = this.getClass().getConstructor(String.class, int.class);
			State s = (State) c.newInstance(this.getName(), this.getID());
			s.setLabel(this.getLabel());
			return s;
		}catch(Exception e){
			throw new RuntimeException(e);
		}

		
	}


	public void setLabel(String label) {
		myLabel = label;
	}

	public boolean clearLabel() {
		boolean b = myLabel != null;
		myLabel = null;
		return b;
	}

	public String getLabel() {
		return myLabel;
	}

	@Override
	public String toString() {
		return this.getName();
	}
	
	public String toDetailedString() {
		return this.getName() + "|id:" + this.getID();
	}

	@Override
	public boolean equals(Object o){
		return this.compareTo((State) o) == 0;
	}

	@Override
	public int compareTo(State toState) {
		return ((Integer)this.getID()).compareTo(toState.getID());
	}

	@Override
	public int hashCode() {
		return (int) (Math.pow(2, this.getID()));
	}



	@Override
	public String getDescriptionName() {
		return "State";
	}



	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}


}
