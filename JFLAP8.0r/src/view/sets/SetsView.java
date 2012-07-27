package view.sets;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.undoing.UndoPanel;

@SuppressWarnings("serial")
public class SetsView extends EditingPanel {
	
	private JComponent myCentralPane;
	
	private DefaultSetPanel myDefaultPanel;
	
	private ActiveSetsList myActiveSetDisplay;

	public SetsView (SetsManager manager) {
		super(new UndoKeeper(), true);
		
		myActiveSetDisplay = new ActiveSetsList(getKeeper());
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
	
	
	public ActiveSetsList getActiveSetDisplay () {
		return myActiveSetDisplay;
	}
	
	
	private void assignObserver(SetsManager manager) {
		manager.setActiveDisplayObserver(myActiveSetDisplay);
	}
	
	
	@Override
	public String getName () {
		return "Sets View";
	}

	
	
	public JComponent createCentralPanel () {
		MagnifiableSplitPane split = new MagnifiableSplitPane(JSplitPane.VERTICAL_SPLIT, null, null);
		
		return null;
	}
	
}
