package view.sets;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.sets.edit.SetsEditingPanel;
import view.undoing.UndoPanel;

@SuppressWarnings("serial")
public class SetsView extends EditingPanel {
	
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
		
		add(new UndoPanel(myKeeper), BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		add(slider, BorderLayout.SOUTH);
	
		assignObserver(manager);
	}
	

	
	private JComponent createCentralPane () {
		MagnifiablePanel main = new MagnifiablePanel();
		myDefaultPanel = new DefaultSetPanel(myKeeper, this);
		myEditingPanel = new SetsEditingPanel(myKeeper);
		myEditingPanel.setVisible(false);
		
		main.add(myDefaultPanel);
		main.add(myEditingPanel);
		return main;
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

	
}
