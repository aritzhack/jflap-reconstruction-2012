package view.sets;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import util.view.magnify.Magnifiable;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.sets.edit.SetsEditingPanel;

@SuppressWarnings("serial")
public class SetsView extends EditingPanel implements Magnifiable {
	
	private UndoKeeper myKeeper;
	private JComponent myCentralPane;
	
	private DefaultSetPanel myDefaultPanel;
	private SetsEditingPanel myEditingPanel;
	
	private ActiveSetDisplay myActiveSetDisplay;

	public SetsView (SetsManager manager) {
		super(new UndoKeeper(), true);
		myKeeper = super.getKeeper();
		
		myActiveSetDisplay = new ActiveSetDisplay(myKeeper);
		
		myCentralPane = createCentralPane();
		
		setLayout(new BorderLayout());	
		
		JScrollPane scroller = new JScrollPane(myCentralPane);
		SizeSlider slider = new SizeSlider(myDefaultPanel);
		slider.distributeMagnification();
		
		add(scroller, BorderLayout.CENTER);
		add(slider, BorderLayout.SOUTH);
	
		assignObserver(manager);
	}
	

	
	private JComponent createCentralPane () {
		myDefaultPanel = new DefaultSetPanel(myKeeper, this);
		
		return myDefaultPanel;
	}
	
	
	public ActiveSetDisplay getActiveSetDisplay () {
		return myActiveSetDisplay;
	}
	
	
	private void assignObserver(SetsManager manager) {
		manager.setActiveDisplayObserver(myActiveSetDisplay);
	}
	
	
	@Override
	public String getName () {
		return "Sets View";
	}

	
	@Override
	public void setMagnification(double mag) {
		for (Component c : this.getComponents()) {
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
	}
	
}
