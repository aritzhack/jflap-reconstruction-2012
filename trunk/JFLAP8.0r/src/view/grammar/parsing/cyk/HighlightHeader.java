package view.grammar.parsing.cyk;

import java.awt.Color;

import model.symbols.Symbol;

public class HighlightHeader {
	private Color highlight;
	private Symbol name;
	
	public HighlightHeader(Symbol name){
		this.name = name;
	}
	
	public Color getHighlight(){
		return highlight;
	}
	
	public void setHightlight(Color highlight){
		this.highlight = highlight;
	}
	
	public String toString(){
		return name.toString();
	}
	
}
