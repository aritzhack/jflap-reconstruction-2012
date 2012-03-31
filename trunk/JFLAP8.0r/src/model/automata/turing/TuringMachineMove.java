package model.automata.turing;

public enum TuringMachineMove {

	RIGHT('R', 1),
	LEFT('L', -1),
	STAY('S', 0);
	
	public char char_abbr;
	public int int_move;

	private TuringMachineMove(char abbr, int move) {
		char_abbr = abbr;
		int_move = move;
	}
	
}
