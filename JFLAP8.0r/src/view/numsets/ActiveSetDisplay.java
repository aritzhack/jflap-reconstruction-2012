package view.numsets;

/**
 * @author Peggy Li
 */

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
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

		initAll();

		JScrollPane scroll = new JScrollPane(myActiveSets);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.setLayout(new BorderLayout());
		this.add(myTitle, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		
	}

	private void initAll() {
		myTitle = new JLabel("Active Sets", JLabel.CENTER);
		
		myActiveSets = new JList();
		
		myActiveSets.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
			}

		});

	}

	
	public void update(Object[] actives) {
		myActiveSets.setListData(actives);
		this.repaint();
	}
	

	public String[] getSelected() {
		
		return (String[]) myActiveSets.getSelectedValues();
	}
	

}
