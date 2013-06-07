package test;

import java.io.File;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.lsystem.CommandAlphabet;
import model.lsystem.LSystem;
import debug.JFLAPDebug;
import file.xml.XMLCodec;
import file.xml.formaldef.lsystem.wrapperclasses.Axiom;
import file.xml.formaldef.lsystem.wrapperclasses.Parameter;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterMap;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterName;
import file.xml.formaldef.lsystem.wrapperclasses.ParameterValue;

public class LSystemFileTester {

	public static void main(String[] args){
		String toSave = System.getProperties().getProperty("user.dir")
				+ "/filetest";
		
		File f = new File(toSave + "/lsystem.jff");
		XMLCodec codec = new XMLCodec();
		
		ParameterName color = new ParameterName("Color"),
					stuff = new ParameterName("Stuff");
		ParameterValue red = new ParameterValue("red"),
					amount = new ParameterValue("50");
		
		Parameter param1 = new Parameter(color, red),
					param2 = new Parameter(stuff, amount);
		ParameterMap parameters = new ParameterMap();
		parameters.put(param1.getName(), param1.getValue());
		parameters.put(param2.getName(), param2.getValue());
		
		Grammar g = new Grammar();
		g.getTerminals().addAll(new CommandAlphabet());
		g.getProductionSet().add(new Production(new Terminal("X"), new Terminal("a")));
		g.getProductionSet().add(new Production(new Terminal("a"), new Terminal("X")));
		g.setStartVariable(new Variable("!"));
		
		Axiom axiom = new Axiom("X a X a X");
		
		LSystem system = new LSystem(axiom, g, parameters);
		
		JFLAPDebug.print("Before import:\n" + system.toString());
		codec.encode(system, f, null);
		system = (LSystem) codec.decode(f);
		JFLAPDebug.print("After import:\n" + system.toString());
	}
	
	
}
