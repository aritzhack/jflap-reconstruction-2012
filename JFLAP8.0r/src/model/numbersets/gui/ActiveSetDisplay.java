package model.numbersets.gui;

/**
 * @author peggyli
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.numbersets.controller.SetEnvironment;

@SuppressWarnings("serial")
public class ActiveSetDisplay extends JPanel {

	private JLabel myTitle;

	private JList myActiveSets;

	public ActiveSetDisplay() {
		
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		initAll();
		
		c.gridx = 0;
		c.gridy = 0;
		this.add(myTitle, c);
		
		c.gridx = 0;
		c.gridy = 1;
		this.add(scrollable(myActiveSets), c);

		this.setBorder(new LineBorder(Color.blue));

		this.setPreferredSize(new Dimension(4000, 6000));
		this.setSize(getPreferredSize());

	}

	private void initAll() {
		myTitle = new JLabel("Active Sets", JLabel.CENTER);

		myActiveSets = new JList();

		myActiveSets.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				update();
			}

		});

	}

	private JComponent scrollable(JComponent target) {
		return new JScrollPane(target, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	
	public void update() {
		String[] actives = SetEnvironment.getInstance().getActiveRegistry().getArray();
		myActiveSets.setListData(actives);

		this.repaint();
	}
	

	public String[] getSelected() {
		System.out.println(myActiveSets.getSelectedValues());
		
		return (String[]) myActiveSets.getSelectedValues();
	}

}
