package model.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import javax.swing.Icon;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Terminal;
import model.grammar.ArrowIcon;

public interface JFLAPConstants {

	public static final Font ALPHABET_BAR_FONT = new Font("Dialog", 1, 15);
	
	public static final Symbol EMPTY_SET_SYMBOL = new Symbol("\u00F8");
	
	public static final Terminal SLR_MARKER = new Terminal("\u00B7");
	
	public static final String JDEF_SUFFIX = ".jdef",
			   JFF_SUFFIX = ".jff",
			   LAMBDA = "\u03BB",
			   EPSILON = "\u03B5",
			   LAMBDA_CODE = "u03BB",
			   EPSILON_CODE = "u03B5";
	

		/**
		 * The arrow icon. This is simply the item returned for the second column.
		 */
		public static Icon ARROW = new ArrowIcon(20, 8);
		
		public static final String DEFAULT_STATE_NAME_PREFIX = "q";

}
