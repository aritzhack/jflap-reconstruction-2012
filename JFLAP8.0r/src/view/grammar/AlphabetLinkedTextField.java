package view.grammar;

import javax.swing.JTextField;


public class AlphabetLinkedTextField extends JTextField 
//implements AlphabetLinked
{

	public AlphabetLinkedTextField(String val) {
		this.setText(val);
	}

	public AlphabetLinkedTextField() {
		this("");
	}

//	@Override
//	public void appendSymbol(Symbol s) {
//		this.replaceSelection(s.toString());
//	}

}
