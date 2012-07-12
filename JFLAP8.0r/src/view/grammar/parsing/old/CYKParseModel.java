package view.grammar.parsing.old;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.testinput.parse.cyk.OldCYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import util.view.magnify.MagnifiablePanel;

public class CYKParseModel extends MagnifiablePanel implements
		ChangeListener {
	private static final int ALG_STEP = 0, INPUT_SET = 1;
	
	private Symbol[] myTarget;
	private Set<Symbol>[][] myTable;
	private OldCYKParser myParser;
	private int editableRow;

	public CYKParseModel(OldCYKParser parser) {
		super(new GridLayout());
		setName("CYK Parse Model");
		
		myParser = parser;
		initBlankModel();

		parser.addListener(this);
	}

	

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof AdvancedChangeEvent
				&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
			update((AdvancedChangeEvent) e);
		}
	}

	private void update(AdvancedChangeEvent e) {
		int type = e.getType();
		if(type == INPUT_SET){
			SymbolString input = (SymbolString) e.getArg(0);
			
			if(input == null) initBlankModel();
			else{
				myTarget = input.toArray(new Symbol[0]);
				initTable(input.size());
			
				setLayout(new GridLayout(0, input.size()));
			}
		}
	}



	private void initBlankModel() {
		myTarget = new Symbol[0];
		myTable = new Set[0][0];
	}



	private void initTable(int size) {
		myTable = new Set[size][size];
		for(int i=0; i < size; i++){
			for(int j=i; j < size; j++){
				myTable[i][j] = new HashSet<Symbol>();
			}
		}
	}

}
