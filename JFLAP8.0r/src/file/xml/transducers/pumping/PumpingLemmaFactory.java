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





package file.xml.transducers.pumping;

import jflap.model.pumping.*;
import jflap.model.pumping.cf.*;
import jflap.model.pumping.reg.*;

/**
 * 
 * @author Jinghui Lim
 *
 */
public class PumpingLemmaFactory 
{
    public static PumpingLemma createPumpingLemma(String type, String name)
    {
        if(type.equals(RegPumpingLemmaTransducer.TYPE))
        {
            if(name.equals(new ABnAk().getName()))
                return new ABnAk();
            else if(name.equals(new AnBkCnk().getName()))
                return new AnBkCnk();
            else if(name.equals(new AnBlAk().getName()))
                return new AnBlAk();
            else if(name.equals(new jflap.model.pumping.reg.AnBn().getName()))
                return new jflap.model.pumping.reg.AnBn();
            else if(name.equals(new AnEven().getName()))
                return new AnEven();
            else if(name.equals(new NaNb().getName()))
                return new NaNb();
            else if(name.equals(new Palindrome().getName()))
                return new Palindrome();
            
            else if (name.equals(new BBABAnAn().getName()))
            	return new BBABAnAn();
            else if (name.equals(new B5W().getName()))
            	return new B5W();
            else if (name.equals(new BkABnBAn().getName()))
            	return new BkABnBAn();
            else if (name.equals(new AnBk().getName()))
            	return new AnBk();
            else if (name.equals(new AB2n().getName()))
            	return new AB2n();
            else if (name.equals(new B5Wmod().getName()))
            	return new B5Wmod();            
            else    // this should not happen 
                return null;
        }
        else if(type.equals(CFPumpingLemmaTransducer.TYPE))
        {
            if(name.equals(new AnBjAnBj().getName()))
                return new AnBjAnBj();
            else if(name.equals(new AiBjCk().getName()))
                return new AiBjCk();
            else if(name.equals(new jflap.model.pumping.cf.AnBn().getName()))
                return new jflap.model.pumping.cf.AnBn();
            else if(name.equals(new AnBnCn().getName()))
                return new AnBnCn();
            else if(name.equals(new NagNbeNc().getName()))
                return new NagNbeNc();
            else if(name.equals(new NaNbNc().getName()))
                return new NaNbNc();
            else if(name.equals(new WW().getName()))
                return new WW();
            
            else if (name.equals(new WW1WrEquals().getName()))
            	return new WW1WrEquals();
            else if (name.equals(new W1BnW2().getName()))
            	return new W1BnW2();
            else if (name.equals(new W1CW2CW3CW4().getName()))
            	return new W1CW2CW3CW4();
            else if (name.equals(new WW1WrGrtrThanEq().getName()))
            	return new WW1WrGrtrThanEq();
            else if (name.equals(new AkBnCnDj().getName()))
            	return new AkBnCnDj();
            else if (name.equals(new W1VVrW2().getName()))
            	return new W1VVrW2();  
            else    // this shouldn't happen
                return null;
        }
        else    // this shouldn't happen
            return null;
    }
}
