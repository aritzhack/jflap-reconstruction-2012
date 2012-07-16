package view.algorithms;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.Steppable;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.steppable.SteppableAlgorithm;
import model.change.events.AdvancedChangeEvent;

public class ThreadedAlgorithm<T extends SteppableAlgorithm> extends SteppableAlgorithm implements ChangeListener{

	private T mySteppable;
	private Thread myThread;
	public static final int THREAD_PAUSED = -3, THREAD_STARTED = -2;
	public ThreadedAlgorithm(T s){
		mySteppable = s;
		myThread = new Thread() {
			
			@Override
			public void run() {
				mySteppable.stepToCompletion();
			}
		};
		myThread.setDaemon(true);
		mySteppable.addListener(this);
	}

	public T getAlgorithm(){
		return mySteppable;
	}

	@Override
	public AlgorithmStep step() {
		return mySteppable.step();
	}

	@Override
	public boolean stepToCompletion() {
		this.resume();
		return true;
	}

	@Override
	public boolean reset() throws AlgorithmException {
		this.pause();
		return mySteppable.reset();
	}

	
	
	public void pause() {
		try {
			myThread.wait();
			distributeChange(new AdvancedChangeEvent(this, THREAD_PAUSED));
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public void resume(){
		myThread.run();
		distributeChange(new AdvancedChangeEvent(this, THREAD_STARTED));
	}

	@Override
	public String getDescriptionName() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return null;
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		this.distributeChange(event);
	}

	public boolean isPaused() {
		return myThread.getState() == Thread.State.WAITING;
	}

	
	
}
