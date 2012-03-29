package model.grammar;

import java.util.Set;

import errors.BooleanWrapper;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.specific.TerminalAlphabet;
import model.formaldef.components.alphabets.specific.VariableAlphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.ProductionSet;
import model.formaldef.components.symbols.StartVariable;

/**
 * An object representing the formal 4-tuple that represents
 * a grammar. This includes the following {@link FormalDefinitionComponent}s:
 * 		{@link TerminalAlphabet}
 * 		{@link VariableAlphabet}
 * 		{@link ProductionSet}
 * 		{@link StartVariable}
 * 
 * Each of these is accessible from the 
 * 
 * @author Julian Genkins
 *
 */
public class Grammar extends FormalDefinition<TerminalAlphabet, ProductionSet> {

	private VariableAlphabet myVariableAlphabet;
	private StartVariable myStartVariable;

	public Grammar(TerminalAlphabet langAlph,
					VariableAlphabet varAlph,
					ProductionSet functions,
					StartVariable startVar) {
		super(langAlph, functions);
		myVariableAlphabet = varAlph;
		myStartVariable = startVar;
	}

	public VariableAlphabet getVariableAlphabet() {
		return myVariableAlphabet;
	}

	@Override
	public String getName() {
		return "Grammar";
	}

	@Override
	public String getDescription() {
		
		return "Hey look, its a Grammar!";
	}

	public TerminalAlphabet getTerminalAlphabet() {
		return super.getLanguageAlphabet();
	}
	
	public StartVariable getStartVariable(){
		return myStartVariable;
	}
	
	public void setStartVariable(Symbol s){
		myStartVariable.setString(s.getString());
	}

	@Override
	public FormalDefinition<TerminalAlphabet, ProductionSet> alphabetAloneCopy() {
		return new Grammar(this.getTerminalAlphabet(), 
							this.getVariableAlphabet(),
							new ProductionSet(),
							new StartVariable());
	}

}
