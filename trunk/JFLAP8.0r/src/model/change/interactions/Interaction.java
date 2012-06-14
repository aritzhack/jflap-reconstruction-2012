package model.change.interactions;

import model.change.ChangeDistributor;
import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.change.ChangeRelated;
import model.change.ChangeTypes;
import model.change.ChangeDistributingObject;
import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.UndoableChangeEvent;

/**
 * An abstract interaction class which is responsible for applying the 
 * full effects of an individual change. 
 * @author Julian
 *
 * @param <T>
 */
public abstract class Interaction extends ChangeRelated{

	private ChangeDistributor myDistributor;

	public Interaction(int type, ChangeDistributor distributor){
		super(type);
		myDistributor = distributor;
	}
	

	public boolean applyInteraction(ChangeEvent e){
		
		boolean changed = e.applyChange();
			
		if (changed){
			CompoundUndoableChangeEvent event = new CompoundUndoableChangeEvent(myDistributor,
					e.getName());
			if (e instanceof UndoableChangeEvent)
				event.addSubEvents((UndoableChangeEvent)e);
			addAndApplyInteractions(e, event);
			myDistributor.distributeChange(event);
		}
		return changed;
	}
	
	/**
	 * Applies any interaction necessary to accomplish this change,
	 * and then returns the "meta" change containing all change 
	 * events applied in this interaction.
	 * 
	 * @param event
	 * @return
	 */
	protected abstract void addAndApplyInteractions(ChangeEvent e, CompoundUndoableChangeEvent event);

	


}
