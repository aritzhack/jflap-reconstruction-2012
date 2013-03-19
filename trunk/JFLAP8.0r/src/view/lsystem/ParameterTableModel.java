/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package view.lsystem;

import java.util.*;

import util.view.tables.GrowableTableModel;

/**
 * A mapping of parameters to values.
 * 
 * @author Thomas Finley
 */

public class ParameterTableModel extends GrowableTableModel {
	/**
	 * Constructs an empty parameter table model.
	 */
	public ParameterTableModel() {
		super(2);
	}

	/**
	 * Constructs a parameter table model out of the map.
	 * 
	 * @param parameters
	 *            the mapping of parameter names to parameter objects
	 */
	public ParameterTableModel(Map<String, String> parameters) {
		this();
		int i=0;
		for (String key : parameters.keySet()) {
			setValueAt(key, i, 0);
			setValueAt(parameters.get(key), i, 1);
			i++;
		}
	}

	@Override
	public boolean checkEmpty(int row) {
		return getValueAt(row, 0).toString().length() == 0 && getValueAt(row, 1).toString().length() == 0;
	}
	
	@Override
	protected Object[] createEmptyRow() {
		return new String[]{"", ""};
	}

	/**
	 * Returns the mapping of names of parameters.
	 * 
	 * @return the mapping from parameter names to parameters (i.e., map of
	 *         contents of the left column to contents of the right column)
	 */
	public TreeMap<String, String> getParameters() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		for (int i = 0; i < getRowCount() - 1; i++) {
			String o = (String) getValueAt(i, 0);
			if (o.equals(""))
				continue;
			map.put(o, (String) getValueAt(i, 1));
		}
		return map;
	}

	/**
	 * Values in the table are editable.
	 * 
	 * @param row
	 *            the row index
	 * @param column
	 *            the column index
	 * @return <CODE>true</CODE> always
	 */
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * Returns the column name.
	 * 
	 * @param column
	 *            the index of the column
	 * @return the name of a particular column
	 */
	public String getColumnName(int column) {
		return column == 0 ? "Name" : "Parameter";
	}
}
