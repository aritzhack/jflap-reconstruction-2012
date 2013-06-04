package model.lsystem.formaldef;

import model.formaldef.components.FormalDefinitionComponent;
import model.symbols.SymbolString;
import errors.BooleanWrapper;

public class Axiom extends FormalDefinitionComponent{

	private SymbolString myAxiom;

	public Axiom(){
		myAxiom = new SymbolString();
	}
	
	public Axiom(SymbolString axiom){
		this.myAxiom = axiom;
	}
	
	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return "Axiom";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Axiom";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return 'A';
	}

	@Override
	public BooleanWrapper isComplete() {
		// TODO Auto-generated method stub
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent copy() {
		// TODO Auto-generated method stub
		return new Axiom(myAxiom);
	}

	@Override
	public void clear() {
		myAxiom = new SymbolString();
	}

}
