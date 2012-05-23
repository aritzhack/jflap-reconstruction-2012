package model.automata;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;

import util.Copyable;

import model.util.JFLAPResources;




public class State implements JFLAPResources, Comparable<State>, Copyable{

	private String myName;

	private String myLabel;

	private int myID;

	private Point myLocation;

	public State(String name, int id){
		this(name, id, new Point(0,0));
	}

	public State(String name, int id, Point point) {
		setName(name);
		setID(id);
		setPoint(point);
	}

	/**
	 * Returns the graphical location of this state
	 * in the automaton transition graph.
	 * 
	 * @return
	 */
	public Point getPoint(){
		return myLocation;
	}
	
	/**
	 * Sets the location of this state to the
	 * passed in point. Only used in graphical
	 * representation of the Automaton.
	 * 
	 * @param point
	 */
	public void setPoint(Point point) {
		myLocation = point;
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