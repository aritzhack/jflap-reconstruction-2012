package file.xml.formaldef.lsystem.wrapperclasses;

/**
 * Special implementation of a String to allow for TransducerFactory to work
 * correctly (maps the proper transducer to the names of Parameters, but not
 * instances of String).
 * 
 * @author Ian McMahon
 * 
 */
public class ParameterName {

	private String name;

	public ParameterName(String s) {
		name = s;
	}

	public String toString() {
		return name;
	}

}
