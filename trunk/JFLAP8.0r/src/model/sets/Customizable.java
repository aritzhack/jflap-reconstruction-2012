package model.sets;

import model.sets.elements.Element;

public interface Customizable {
	
	public void setName (String name);
	
	public void setDescription (String description);
	
	
	public void add (Element e);
	
	public void remove (Element e);
	

}
