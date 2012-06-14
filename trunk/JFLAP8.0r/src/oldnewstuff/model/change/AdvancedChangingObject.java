package oldnewstuff.model.change;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import oldnewstuff.model.change.interactions.Interaction;
import oldnewstuff.model.change.rules.FormalDefinitionRule;
import oldnewstuff.model.change.rules.GroupingRule;
import oldnewstuff.model.change.rules.Rule;

import errors.BooleanWrapper;
import errors.JFLAPException;


public class AdvancedChangingObject extends ChangeApplyingObject {

	private Set<Rule> myRules;
	private TreeSet<Interaction> myInteractions;

	public AdvancedChangingObject(){
		myRules = new TreeSet<Rule>();
		myInteractions = new TreeSet<Interaction>();
	}
	
	public Interaction[] getInteractions() {
		return myInteractions.toArray(new Interaction[0]);
	}
	
	public boolean removeInteractions(Interaction interaction){
		return myInteractions.remove(interaction);
	}

	public boolean addInteractions(Interaction ... interactions){
		return myInteractions.addAll(Arrays.asList(interactions));
	}
	
	public Rule[] getRules() {
		return myRules.toArray(new Rule[0]);
	}
	
	public boolean removeRule(Rule rule){
		return myRules.remove(rule);
	}

	public boolean addRules(Rule ... rules){
		return myRules.addAll(Arrays.asList(rules));
	}
	
	public <T extends GroupingRule> GroupingRule getRuleOfClass(Class<T> clz) {
		for (Rule rule : this.getRules()){
			if (clz.isAssignableFrom(rule.getClass()))
				return clz.cast(rule);
		}
		return null;
	}
	
	@Override
	public boolean applyChange(ChangeEvent e) {
		if (!e.comesFrom(this))
			return false;
		
		BooleanWrapper ruleCheck = checkRules(e);
		if (ruleCheck.isError())
			throw new JFLAPException(ruleCheck.getMessage());
		
		boolean changed = this.applyInteractions(e);
		
		if (changed)
			distributeChange(e);
		return changed;
	}

	private boolean applyInteractions(ChangeEvent e) {
		for (Interaction i: myInteractions){
			if (i.appliesTo(e)){
				return i.applyInteraction(e);
			}
		}
		return e.applyChange();
	}

	private BooleanWrapper checkRules(ChangeEvent e) {
		BooleanWrapper bw = new BooleanWrapper(true);
		for (Rule rule: myRules){
			if (rule.appliesTo(e))
				bw = rule.checkRule(e);
			if (bw.isError())
				break;
		}
		return bw;
	}
	

	public void clearInteractions() {
		myInteractions.clear();
	}
	
	public void clearRules(){
		myRules.clear();
	}
	
}
