/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */




package model.grammar.parsing.brute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.TreeNode;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.ParseNode;




/**
 * Similar to BruteParser abstract class, UserParser abstract class is created to deal with User Parsing
 * 
 * NOTE: This code is very similar to BruteParser and it would be better to combine two classes and extract hiearchy.
 *       However, since Brute Parser is fully functional, I did not want to mess wtih BruteParser class.
 *       
 * @author Kyung Min (Jason) Lee
 *
 */
public abstract class UserParser extends BaseParser
{
	
	/**
	 * Constructor for UserParser abstract class.
	 * This is intialized by sub-classes of UserParser class.
	 * 
	 * @param grammar The grammar that is going to be used for parsing
	 * @param target The target string that user is trying to derive
	 */
	public UserParser(Grammar grammar) {
		super(grammar);
	}


	/**
	 * This factory method will return a user parser appropriate for the
	 * grammar.
	 * 
	 * @param grammar
	 *            the grammar to get a brute force parser for
	 * @param target
	 *            the target string
	 */
	public static UserParser get(Grammar grammar) {
		if (Unrestricted.isUnrestricted(grammar))
		{
			return new UnrestrictedUserParser(grammar);
		}
		return new RestrictedUserParser(grammar);
	}
	
	/**
	 * Given an index of Production rule array,
	 * this method finds LHS variable of chosen production rule and 
	 * count how many of LHS variable is present in our current String. 
	 * 
	 * @param index Index of our production rule that is selected by user.
	 * @return Return the count of LHS variables present in the String.
	 */
	public int checkValidAndParse(int index)
	{
	/*	for (int i=0; i<myProductions.length; i++)
		{
			System.out.println(" index : "+i+ "  production = "+myProductions[i]);
		}*/
		
		myCurrentProduction=myProductions[index];
		int length=myCurrentProduction.getLHS().size();
		int count=0;
		for (int i=0; i+length<=myAnswer.getDerivation().size();)
		{
			if (myAnswer.getDerivation().subList(i,i+length).equals(myCurrentProduction.getLHS()))
			{
				i += length;
				count++;
			}
			else
				i++;
		}
		return count;
	}

	
	/**
	 * Returns a Next possible one step parse for a given string. 
	 * The first entry is always the identity.
	 * 
	 * @param c the current String
	 * @param index the index of String where we are going to apply the production rule
	 */
	private ParseNode getNextResult(SymbolString c, int index) {
		if (c.size() == 0) {
			return E;
		}
		
		// Find the start of the production.
		int start=0;
		
		if (index<0)
			index=c.indexOf(myCurrentProduction.getLHS());
		//System.out.println("MY Current Production = "+myCurrentProduction);
		
		//System.out.println("MY RHS = "+myCurrentProduction.getRHS());
		
		start=index;
		SymbolString prepend = SymbolString.concat(c.subList(0, start),
				myCurrentProduction.getRHS(),
				c.subList(start+myCurrentProduction.getLHS().size()));
		Production[] singleProductionArray=new Production[1];
		singleProductionArray[0]=myCurrentProduction;
		int[] singleSubstitutionArray=new int[1];
		singleSubstitutionArray[0]=start;
		return new ParseNode(prepend, singleProductionArray, singleSubstitutionArray);
	}

	/**
	 * Checks whether it is possible derivation or not given a String
	 * @param derivation String that is going to be checked
	 * @return True for possible derivation and false for impossible derivation.
	 */
	public boolean isPossibleDerivation(SymbolString derivation) {
		return Unrestricted.minimumLength(derivation, mySmallerSet) <= myTarget
				.size();
	}
	
	/**
	 * The parsing method.
	 */
	public synchronized void parse(int index) {
		if (myCount==0) {
			myCount++;
			return;
		}
		
		ParseNode node=(ParseNode) myQueue.removeFirst();
		ParseNode pNode=getNextResult(myAnswer.getDerivation(), index);
		pNode = new ParseNode(pNode);

		node.add(pNode);
		myQueue.add(pNode);
		myAnswer=pNode;
		if (pNode.getDerivation().equals(myTarget)) {
			isDone = true;
			return;
		}
		isDone=false;
	}
	
	
	/**
	 * This will start the parsing. This method will return immediately. The
	 * parsing is done in a separate thread since the potential for the parsing
	 * to take forever on some brute force parses exists.
	 * 
	 * @return if the starting of the parsing was successful, which will not be
	 *         successful if the parsing is already underway, or if the parser
	 *         is finished
	 */
	public boolean start() {
		if (isFinished())
			return false;
		parse(-1);
		return true;
	}


	


	/**
	 * Given selectedRow index, this method returns the LHS variable of the selected prodcution
	 * @param selectedRow Row that user selected
	 * @return LHS variable of selected production.
	 */
	public SymbolString getLHSForProduction(int selectedRow) {
		return myProductions[selectedRow].getLHS();
	}

	/**
	 * This method is similar to parse method.
	 * However, this method is called whenever more than two variables are going to be applied with same production
	 * at same time 
	 * @param tempIndices Indices of where the substitution will occur
	 */
	public void subsitute(int[] tempIndices) {
		ParseNode node=(ParseNode) myQueue.removeFirst();
		ParseNode pNode= getNextSubstitution(myAnswer.getDerivation(), tempIndices);

		pNode = new ParseNode(pNode);
	
		node.add(pNode);
		myQueue.add(pNode);
		myAnswer=pNode;
			
		if (pNode.getDerivation().equals(myTarget)) {
			isDone = true;
			return;
		}
		isDone=false;
	}

	/**
	 * This method is called when one production is applied to multiple variable.
	 * It is similar to getNextResult method.
	 * However, unlike getNextResult method, this one creates multiple production and substituion array.
	 * 
	 * @param symbolString Current String
	 * @param tempIndices Indices of where the substitution will occur
	 * @return The next parseNode derived from this production
	 */
	private ParseNode getNextSubstitution(SymbolString symbolString, int[] tempIndices) {
		// Find the start of the production.
		int[] multipleSubstitutionArray=tempIndices;
		
		int start=0;
		SymbolString prepend= new SymbolString();
		Production[] multipleProductionArray=new Production[multipleSubstitutionArray.length];
		multipleProductionArray[0]=myCurrentProduction;
		for (int i=0; i<multipleSubstitutionArray.length; i++)
		{
			if (i==0)
				start=multipleSubstitutionArray[i];
			else
				start=multipleSubstitutionArray[i]+i*(myCurrentProduction.getRHS().size()-1);
			prepend = SymbolString.concat(symbolString.subList(0, start),
					myCurrentProduction.getRHS(),
					symbolString.subList(start+myCurrentProduction.getLHS().size()));
			symbolString=prepend;
			multipleProductionArray[i]=myCurrentProduction;
		}

		return new ParseNode(prepend, multipleProductionArray, multipleSubstitutionArray);
	}

	/**
	 * This method whether the user have reached the final step regardless of the acceptance of the String.
	 * In other words, if there is no more variable to apply the production rule, 
	 * this method returns false
	 * @param symbolString current String that user have derived
	 * @return True is more production is possible, false is there are no variables left.
	 */
	public boolean isStringTerminal(SymbolString symbolString) {
		for (int i=0; i<myProductions.length; i++)
		{
			if (symbolString.contains(myProductions[i].getLHS()))
				return false;
		}
		return true;
	}


	@Override
	public String getDescriptionName() {
		return "User Parse";
	}

	
}
