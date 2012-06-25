package model.automata.turing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.library.ShiftBlock;
import model.automata.turing.buildingblock.library.SingleShiftBlock;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;



public class TapeAlphabet extends Alphabet{

	@Override
	public String getDescriptionName() {
		return "Tape Alphabet";
	}

	@Override
	public Character getCharacterAbbr() {
		return '\u0915';
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSymbolName() {
		return "Symbol";
	}
	
	@Override
	public TapeAlphabet copy() {
		return (TapeAlphabet) super.copy();
	}

}
