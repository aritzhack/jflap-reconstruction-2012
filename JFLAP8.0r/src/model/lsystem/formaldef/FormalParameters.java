package model.lsystem.formaldef;

import java.util.HashMap;
import java.util.Map;

import model.formaldef.components.FormalDefinitionComponent;
import errors.BooleanWrapper;

public class FormalParameters extends FormalDefinitionComponent{
	
	private Map<String, String> parameters;
	
	public FormalParameters(){
		this.parameters = new HashMap<String, String>();
	}
	
	public FormalParameters(Map<String, String> parameters){
		this.parameters = parameters;
	}
	
	public Map<String, String> getParameters(){
		return parameters;
	}

	@Override
	public String getDescriptionName() {
		return "Parameters";
	}

	@Override
	public String getDescription() {
		return "Parameters for L-System";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return 'P';
	}

	@Override
	public BooleanWrapper isComplete() {
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent copy() {
		Map<String, String> copy = new HashMap<String, String>();
		copy.putAll(parameters);
		return new FormalParameters(copy);
	}

	@Override
	public void clear() {
		parameters.clear();
	}

}
