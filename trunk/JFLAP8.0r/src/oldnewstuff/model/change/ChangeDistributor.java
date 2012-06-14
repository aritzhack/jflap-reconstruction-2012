package oldnewstuff.model.change;

/**
 * An interface to designate if something is capable of distributing a 
 * change event.
 * @author Julian
 *
 */
public interface ChangeDistributor {

	public void distributeChange(ChangeEvent e);
}
