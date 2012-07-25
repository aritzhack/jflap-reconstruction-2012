package view.sets;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.sets.edit.ElementsBar;
import view.sets.edit.SetsEditingPanel;
import view.undoing.UndoPanel;

@SuppressWarnings("serial")
public class SetsView extends EditingPanel {
	
	private JComponent myCentralPane;
	
	private DefaultSetPanel myDefaultPanel;
	
	private ActiveSetDisplay myActiveSetDisplay;

	public SetsView (SetsManager manager) {
		super(new UndoKeeper(), true);
		
		myActiveSetDisplay = new ActiveSetDisplay(getKeeper());
		myCentralPane = createCentralPane();
		
		setLayout(new BorderLayout());	
		
		JScrollPane scroller = new JScrollPane(myCentralPane);
		SizeSlider slider = new SizeSlider(myDefaultPanel);
		slider.distributeMagnification();
		
		add(new UndoPanel(getKeeper()), BorderLayout.NORTH);
		add(scroller, BorderLayout.CENTER);
		add(slider, BorderLayout.SOUTH);
	
		assignObserver(manager);
	}
	

	
	private JComponent createCentralPane () {
		MagnifiablePanel main = new MagnifiablePanel();
		myDefaultPanel = new DefaultSetPanel(getKeeper(), this);
		main.add(myDefaultPanel);
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
