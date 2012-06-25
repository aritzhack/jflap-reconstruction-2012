package model.numbersets.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.numbersets.AbstractNumberSet;

public class SelectedSetsRegistry extends SetRegistry {

	private ArrayList<AbstractNumberSet> mySelectedSets;

	public SelectedSetsRegistry() {
		mySelectedSets = new ArrayList<AbstractNumberSet>();
	}

	@Override
	public void remove(AbstractNumberSet s) {
		mySelectedSets.remove(s);
	}

	@Override
	public void add(AbstractNumberSet s) {
		mySelectedSets.add(s);
	}


	@Override
	public String[] getArray(ArrayList<AbstractNumberSet> sets) {
		return super.getArray(sets);
	}
}
