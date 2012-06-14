package model.grammar.parsing.lr;

import oldnewstuff.universe.preferences.JFLAPPreferences;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;

public class SLR1Production extends Production {

	private int myMarkIndex;

	public SLR1Production(Production p, int i){
		super(p.getLHS(), constructRHS(p.getRHS()));
			myMarkIndex = i;
	}

	private static SymbolString constructRHS(SymbolString rhs) {
		if (rhs.isEmpty()){
			rhs = new SymbolString(JFLAPPreferences.getSubForEmptyString());
		}
		return rhs;
	}

	public SLR1Production(Production p){
		this(p, p.isLambdaProduction() ? 1 : 0);
	}
	
	@Override
	public SymbolString getRHS() {
		SymbolString newRHS = new SymbolString(super.getRHS());
		newRHS.add(myMarkIndex, SLR_MARKER);
		return newRHS;
	}
	
	public Production createNormalProduction(){
		SymbolString lhs = new SymbolString(this.getLHS());
		SymbolString rhs = getMarkerFreeRHS();
		return new Production(lhs, rhs);
	}

	private SymbolString getMarkerFreeRHS() {
		SymbolString rhs = new SymbolString(getRHS());
		rhs.remove(SLR_MARKER);
		rhs.remove(JFLAPPreferences.getSubForEmptyString());
		return rhs;
	}
	
	public Symbol getSymbolAfterMarker(){
		if (isReduceProduction())
			return null;

		return super.getRHS().get(myMarkIndex);	
	}

	public boolean isReduceProduction() {
		return myMarkIndex == super.getRHS().size();
	}

	public void shiftMarker() {
		myMarkIndex++;
	}
	
	@Override
	public SLR1Production copy() {
		return new SLR1Production(this.createNormalProduction(), myMarkIndex);
	}
	
}
