package view.numsets.actions;

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

import view.numsets.SetOperationHelper;

import model.numbersets.AbstractNumberSet;
import model.numbersets.operations.Intersection;
import model.numbersets.operations.SetOperation;
import model.numbersets.operations.Union;


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





}
