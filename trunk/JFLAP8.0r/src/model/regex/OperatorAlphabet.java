package model.regex;

import universe.preferences.JFLAPPreferences;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.KleeneStar;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;

public class OperatorAlphabet extends Alphabet {

	private UnionOperator myUnionOperator;
	private OpenGroup myOpenGroup;
	private KleeneStar myKleeneStar;
	private CloseGroup myCloseGroup;

	public OperatorAlphabet(){
		super();
		myUnionOperator = new UnionOperator();
		myKleeneStar = new KleeneStar();
		myOpenGroup = JFLAPPreferences.getCurrentRegExOpenGroup();
		myCloseGroup = JFLAPPreferences.getCurrentRegExCloseGroup();
		this.addAll(myUnionOperator, myKleeneStar, myOpenGroup, myCloseGroup);
	}
	
	public OpenGroup getOpenGroup(){
		return myOpenGroup;
	}
	
	public CloseGroup getCloseGroup(){
		return myCloseGroup;
	}
	
	public KleeneStar getKleeneStar(){
		return myKleeneStar;
	}
	
	public UnionOperator getUnionOperator(){
		return myUnionOperator;
	}
	
	@Override
	public String getDescriptionName() {
		return "Operators";
	}

	@Override
	public String getDescription() {
		return "The set of all operators possible in a regular expression.";
	}

	@Override
	public String getSymbolName() {
		return "operator";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'O';
	}

}