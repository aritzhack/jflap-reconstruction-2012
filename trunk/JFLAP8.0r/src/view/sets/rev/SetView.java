package view.sets.rev;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.action.sets.ActivateSetAction;
import view.sets.ActiveSetsList;
import view.sets.OperationsToolbar;
import view.undoing.UndoPanel;

@SuppressWarnings("serial")
public class SetView extends EditingPanel {
	
	private ActiveSetsList myActiveList;
	private JComponent myCentralPanel;
	private UndoKeeper myKeeper;
	private SizeSlider slider;

	public SetView(SetsManager manager) {
		this(manager, new UndoKeeper(), true);
	}
	
	public SetView(SetsManager manager, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		setLayout(new BorderLayout());
		
		myKeeper = keeper;
		myCentralPanel = createCentralPanel();
		
		slider = new SizeSlider();
		if (myCentralPanel instanceof Magnifiable) 
			slider.addListener((Magnifiable) myCentralPanel);
		slider.distributeMagnification();
		this.add(slider, BorderLayout.SOUTH);
		
		this.add(myCentralPanel, BorderLayout.CENTER);
		
		if (editable) {
			add(new UndoPanel(myKeeper), BorderLayout.NORTH);
		}
		
		setAsObserveable(manager);
	}
	
	private JComponent createCentralPanel() {
		myActiveList = new ActiveSetsList(myKeeper);
		JScrollPane scroller = new MagnifiableScrollPane(myActiveList);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(new OperationsToolbar());
		panel.add(scroller);
		panel.add(new MagnifiableButton(new ActivateSetAction(myKeeper), JFLAPPreferences.getDefaultTextSize()), Alignment.CENTER);
		return panel;
		
//		OperationsToolbar toolbar = new OperationsToolbar();
//		MagnifiableButton button = new MagnifiableButton(new ActivateSetAction(myKeeper), 
//				JFLAPPreferences.getDefaultTextSize());
////		button.setMaximumSize(new Dimension(this.getWidth(), JFLAPPreferences.getDefaultTextSize() * 2));
//		
////		MagnifiableSplitPane subsplit = new MagnifiableSplitPane(JSplitPane.VERTICAL_SPLIT, scroller, button);
//		MagnifiablePanel panel = new MagnifiablePanel();
//		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
////		panel.add(toolbar);
//		panel.add(scroller);
//		panel.add(button);
//		MagnifiableSplitPane split = new MagnifiableSplitPane(JSplitPane.VERTICAL_SPLIT, toolbar, panel);
//		return split;
	}
	
	@Override
	public String getName() {
		return "Sets View";
	}

	
	public void setAsObserveable(SetsManager observer) {
		observer.setActiveDisplayObserver(myActiveList);
	}
}
