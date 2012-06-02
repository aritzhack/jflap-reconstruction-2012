package model.automata.turing;

import java.util.ArrayList;
import java.util.List;


import javax.swing.JOptionPane;

import model.formaldef.components.FormalDefinitionComponent;



public class TapeAlphabet extends model.formaldef.components.alphabets.Alphabet{

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
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public TapeAlphabet copy() {
		return (TapeAlphabet) super.copy();
	}

}
