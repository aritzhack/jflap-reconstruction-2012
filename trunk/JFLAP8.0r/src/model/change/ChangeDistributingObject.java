package model.change;

import java.util.HashSet;
import java.util.Set;


public class ChangeDistributingObject implements ChangeDistributor{

	
	private Set<ChangeListener> myListeners;
	
	public ChangeDistributingObject() {
		myListeners = new HashSet<ChangeListener>();
	}
	
	public boolean addListener(ChangeListener listener){
		return myListeners.add(listener);
	}
	
	public void clearListeners(){
		myListeners.clear();
	}
	
	public boolean removeListener(ChangeListener listener){
		return myListeners.remove(listener);
	}
	
	@Override
	public void distributeChange(ChangeEvent event) {
		for (ChangeListener listener: myListeners){
			listener.stateChanged(event);
		}
	}
}
