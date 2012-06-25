package model.numbersets.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.numbersets.AbstractNumberSet;
import model.numbersets.CustomSet;
import model.numbersets.controller.SetEnvironment;

@SuppressWarnings("serial")
public class ActiveSetDisplay extends JPanel {

	private JLabel myTitle;

	private JList myActiveSets;

	public ActiveSetDisplay() {

		this.setLayout(new GridLayout(0, 1));
		initAll();

		this.add(myTitle);
		this.add(myActiveSets);

	}

	private void initAll() {
		myTitle = new JLabel("Active Sets", JLabel.CENTER);
		
		myActiveSets = new JList();
		
		
		myActiveSets.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				JList list = (JList) e.getSource();
				
				
			}
		
		});
		
		
//		myActiveSets.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("Mouse clicked " + ((JList) arg0.getSource()).getSelectedIndex());
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mouseExited(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mousePressed(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
	
	}

	private void update() {
//		myActiveSets.setListData(SetEnvironment.getSetEnvironment().getActiveRegistry().getArray());
		
		String[] a = {"test", "data"};
		myActiveSets.setListData(a);
		this.repaint();
	}

	private String[] getSelected() {
		return (String[]) myActiveSets.getSelectedValues();
	}

	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame();
		
		ActiveSetDisplay disp = new ActiveSetDisplay();
		
		frame.add(disp);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//disp.update();
		
		CustomSet s = new CustomSet();
		s.setName("Custom1");
		SetEnvironment.getSetEnvironment().getActiveRegistry().add(s);
		System.out.println(SetEnvironment.getSetEnvironment().getActiveRegistry().getArray(null).length);
		Thread.sleep(5000);
		
		disp.update();
		
	}
}
