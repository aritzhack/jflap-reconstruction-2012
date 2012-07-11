package view.numsets;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;


import model.numbersets.operations.Intersection;
import model.numbersets.operations.SetOperation;
import model.numbersets.operations.Union;
import model.sets.AbstractNumberSet;


@SuppressWarnings("serial")
public class SetOperationButton extends JButton {


	public SetOperationButton (String name, Image icon) {
		super(new ImageIcon(icon));

		this.setToolTipText(name);
	}


	public SetOperationButton (String name) {
		super(name);

		this.setToolTipText(name);
		this.setPreferredSize(new Dimension(40, 40));
		this.setSize(getPreferredSize());

	}





}
