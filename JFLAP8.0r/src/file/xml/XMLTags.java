package file.xml;

public interface XMLTags {

	public static final String
	
		TRANS_INPUT_TAG = "input",
		PDA_POP_TAG = "pop",
		PDA_PUSH_TAG = "push",
		OUTPUT_TAG = "output",
		TM_READ_TAG = "read",
		TM_WRITE_TAG ="write",
		RHS_TAG = "rhs",
		LHS_TAG = "lhs",
		TRANSITION_TAG = "transition",
		FROM_STATE = "from",
		TO_STATE = "to",
		INPUT_ALPH = "input_alph",
		TRANS_SET = "transition_set",
		STATE_SET_TAG = "state_set",
		START_STATE = "start_state",
		STRUCTURE_TYPE_ATTR="type",
		STRUCTURE_TAG = "structure",
		FSA_TAG="fsa",
		FINAL_STATESET_TAG = "final_states",
		OUTPUT_FUNC_SET = "output_set";
	
	public static final String ID_TAG = "id";
	public static final String NAME_TAG = "name";
	public static final String STATE_TAG = "state";
	public static final String VARIABLES_TAG = "variable_alph";
	public static final String TERMINALS_TAG = "terminal_alph";
	public static final String START_VAR_TAG = "start_var";
	public static final String PROD_SET_TAG = "production_set";
	public static final String EXPRESSION_TAG = "exp";
	public static final String REGEX = "regex";



	
}
