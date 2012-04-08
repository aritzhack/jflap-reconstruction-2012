package model.automata;

import java.awt.Point;
import java.lang.reflect.Constructor;


import util.JFLAPResources;


public class State implements JFLAPResources, Comparable<State>{

	private String myName;

	private String myLabel;

	private int myID;

	private Point myLocation;


	public State(String name, int id, Point location){
		myName = name;
		myID = id;
		myLocation = location;
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
	 * Returns the simple "name" for this state. By default this will simply be
	 * "qd", where d is the ID number.
	 * 
	 * @return the name for this state
	 */
	public String getName() {
		if (myName == null) {
			myName = DEFAULT_STATE_NAME_PREFIX + Integer.toString(getID());
		}
		return myName;
	}

	/**
	 * Returns the point this state is centered on in the canvas.
	 * 
	 * @see #setLocation(Point)
	 * @return the point this state is centered on in the canvas
	 */
	public Point getLocation() {
		return myLocation;
	}

	/**
	 * Sets the point for this state.
	 * 
	 * @param point
	 *            the point this state is moving to
	 * @see #getLocation()
	 */
	public void setLocation(Point point) {
		this.myLocation = point;
	}

	public int getID(){
		return myID;
	}
	
	public void setID(int id){
		myID = id;
	}

	@Override 
	public State clone(){
		try{
			Constructor c = this.getClass().getConstructor(String.class, int.class, Point.class);
			State s = (State) c.newInstance(this.getName(), this.getID(), this.getLocation());
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

	public boolean equals(State s){
		return this.compareTo(s) == 0;
	}

	@Override
	public int compareTo(State toState) {
		return ((Integer)this.getID()).compareTo(toState.getID());
	}

	@Override
	public int hashCode() {
		return (int) (Math.pow(2, this.getID()));
	}


}
