package view.numsets;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import model.numbersets.control.SetsManager;
import model.undo.UndoKeeper;
import view.EditingPanel;
import view.grammar.Magnifiable;

@SuppressWarnings("serial")
public class SetView extends EditingPanel implements Magnifiable {
	
	private JComponent myCentralPane;
	
	private ActiveSetDisplay myActiveSetDisplay;

	public SetView (SetsManager manager) {
		super(new UndoKeeper(), true);
		
		myCentralPane = createCentralPane();
		
		this.setLayout(new BorderLayout());
		this.add(myCentralPane, BorderLayout.CENTER);
		
		assignView(manager);
		
	}
	
	
	private JComponent createCentralPane () {
		myCentralPane = new JPanel();
		myCentralPane.setLayout(new BoxLayout(myCentralPane, BoxLayout.Y_AXIS));
		
		myActiveSetDisplay = new ActiveSetDisplay();
		
		myCentralPane.add(myActiveSetDisplay);
		myCentralPane.add(new SetInitiationPanel());
		myCentralPane.add(new OperationsPanel());
		
		return myCentralPane;
	}
	
	
	private void assignView(SetsManager manager) {
		manager.setView(this);
	}
	
	public ActiveSetDisplay getActiveSetDisplay() {
		return myActiveSetDisplay;
	}
	
	
	public String getName () {
		return "Sets View";
	}

	@Override
	public void setMagnification(double mag) {
			
	}
	
}
