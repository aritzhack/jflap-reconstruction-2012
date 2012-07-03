package view.numsets.actions;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class ActionButton extends JButton {

	public ActionButton (String name, Image icon) {
		super(new ImageIcon(icon));

		this.setToolTipText(name);

	}


	public ActionButton (String name) {
		super(name);

		this.setToolTipText(name);
		this.setPreferredSize(new Dimension(40, 40));
		this.setSize(getPreferredSize());
	}


	public void addListener () {
		// TODO: parameter
		
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				

			}
		});
	}


}
