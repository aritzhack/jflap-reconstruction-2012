package view.sets;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import debug.JFLAPDebug;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import util.view.magnify.Magnifiable;
import util.view.magnify.SizeSlider;
import view.EditingPanel;

@SuppressWarnings("serial")
public class SetsView extends EditingPanel implements Magnifiable {
	
	private UndoKeeper myKeeper;
	private JComponent myCentralPane;
	
	private JComponent myDefaultPanel;
	private JComponent myEditingPanel;
	
	private ActiveSetDisplay myActiveSetDisplay;

	public SetsView (SetsManager manager) {
		super(new UndoKeeper(), true);
		myKeeper = super.getKeeper();
		myActiveSetDisplay = new ActiveSetDisplay();
		myCentralPane = createCentralPane();
		
		setLayout(new BorderLayout());	
		
		JScrollPane scroller = new JScrollPane(myCentralPane);
		SizeSlider slider = new SizeSlider();
		slider.distributeMagnification();
		
		add(scroller, BorderLayout.CENTER);
		add(slider, BorderLayout.SOUTH);
	
		assignObserver(manager);
	}
	
	
	private JComponent createCentralPane () {
		myCentralPane = new JPanel();
		
		myCentralPane.setLayout(new BoxLayout(myCentralPane, BoxLayout.X_AXIS));
		myCentralPane.add(new DefaultSetPanel(myKeeper, this));
		
		return myCentralPane;
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
