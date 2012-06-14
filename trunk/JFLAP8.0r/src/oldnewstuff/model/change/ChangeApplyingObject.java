package oldnewstuff.model.change;

public class ChangeApplyingObject extends ChangeDistributingObject {

	public boolean applyChange(ChangeEvent e){
		boolean changed = false;
		if (e.comesFrom(this))
			changed = e.applyChange();
		if (changed)
			distributeChange(e);
		return changed;
	}
	
}
