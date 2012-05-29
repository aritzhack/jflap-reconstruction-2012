package model.algorithms.conversion.regextofa;

import java.util.List;

import model.algorithms.AlgorithmException;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;

/**
 * An interface designed to provide some syntactic sugar for the 
 * implementation of the RE to NFA conversion class. This
 * also makes it fairly easy to implement different definitions
 * of the operators in an RE.
 * 
 * @author Julian Genkins
 *
 */
public abstract class DeExpressionifier {
	
	private OperatorAlphabet myOpAlph;

	public DeExpressionifier(OperatorAlphabet alph){
		myOpAlph = alph;
	}
	
	/**
	 * Checks to see if this DeExpressionifier should be applied
	 * to the input transition, i.e. is the input for the transition
	 * flanked by parenthesis? does it end in a kleene star? etc.
	 * 
	 * @param trans
	 * @return
	 */
	public boolean isApplicable(FSTransition trans){
		SymbolString input = trans.getInput();
		SymbolString first = this.getFirstOperand(input);
		return isApplicable(first, input.subList(first.size()));
	}
	
	/**
	 * Checks if the deExpressionify is applicable to the input when split
	 * into the first operand and remaining symbols.
	 * 
	 * @param first
	 * @param rest
	 * @return
	 */
	protected abstract boolean isApplicable(SymbolString first, SymbolString rest);

	
	/**
	 * Adjusts the GTG to apply the deExpressionifying of the transition
	 * and returns the transitions necessary to complete the connectivity
	 * of the resulting GTG;
	 * 
	 * @param trans
	 * @param transSet
	 * @return
	 */
	public abstract List<FSTransition> adjustTransitionSet(
											FSTransition trans,
											GeneralizedTransitionGraph gtg);
	
	
	/**
	 * Retrieves the first operand from the input SymbolString.
	 * 
	 * @param input
	 * @return
	 */
	protected SymbolString getFirstOperand(SymbolString input) {
		int n = 0;
		int i = 0;
		for (; i < input.size(); i++){
			Symbol s = input.get(i);
			if (myOpAlph.getOpenGroup().equals(s))
				n++;
			else if(myOpAlph.getCloseGroup().equals(s)) 
				n--;
			
			if (n == 0)
				break;
		}
		
		if (i < input.size()-1 && 
				input.get(i+1).equals(myOpAlph.getKleeneStar())){
			i++;
		}
		return input.subList(0,i+1);
	}
}
