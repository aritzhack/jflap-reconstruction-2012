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

import java.io.Serializable;
import java.util.ArrayList;

import jflap.file.xml.transducers.PumpingLemmaTransducer;
import jflap.model.pumping.*;


import org.w3c.dom.*;


/**
 * This is the transducer for encoding and decoding 
 * {@link jflap.model.pumping.RegularPumpingLemma} objects.
 * 
 * @author Jinghui Lim
 * @see jflap.view.pumping.PumpingLemmaChooser
 *
 */
public class RegPumpingLemmaTransducer extends PumpingLemmaTransducer<RegularPumpingLemma> 
{
    /**
     * The type of pumping lemma.
     */
    public static String TYPE = "regular pumping lemma";
    /**
     * The tag for the length of <i>x</i>.
     */
    public static String X_NAME = "xLength";
    /**
     * The tag for the length of <i>y</i>.
     */
    public static String Y_NAME = "yLength";
    
    public RegularPumpingLemma fromStructureRoot(Element root) 
    {
        RegularPumpingLemma pl = (RegularPumpingLemma)PumpingLemmaFactory.createPumpingLemma
            (TYPE, root.getElementsByTagName(LEMMA_NAME).item(0).getTextContent());
        
        //Decode m, w, & i.         
        pl.setM(Integer.parseInt(root.getElementsByTagName(M_NAME).item(0).getTextContent()));
        pl.setW(root.getElementsByTagName(W_NAME).item(0).getTextContent());
        pl.setI(Integer.parseInt(root.getElementsByTagName(I_NAME).item(0).getTextContent()));
        
        //Decode the attempts
        NodeList attempts = root.getElementsByTagName(ATTEMPT);
        for(int i = 0; i < attempts.getLength(); i++)
            pl.addAttempt(attempts.item(i).getTextContent());
        
        //Decode the first player.        
        pl.setFirstPlayer(root.getElementsByTagName(FIRST_PLAYER).item(0).getTextContent());
        
        //Decode the decomposition.
        int xLength = Integer.parseInt(root.getElementsByTagName(X_NAME).item(0).getTextContent());
        int yLength = Integer.parseInt(root.getElementsByTagName(Y_NAME).item(0).getTextContent());
        pl.setDecomposition(new int[]{xLength, yLength});
        
        return pl;
    }
    
    @Override
    public Document toDOM(RegularPumpingLemma structure) 
    {   
        RegularPumpingLemma pl = (RegularPumpingLemma)structure;
        Document doc = newEmptyJFLAPDocument();
        Element elem = doc.getDocumentElement();        
        elem.appendChild(createElement(doc, LEMMA_NAME, null, pl.getName()));
        elem.appendChild(createElement(doc, FIRST_PLAYER, null, pl.getFirstPlayer()));
        elem.appendChild(createElement(doc, M_NAME, null, "" + pl.getM()));
        elem.appendChild(createElement(doc, W_NAME, null, "" + pl.getW()));
        elem.appendChild(createElement(doc, I_NAME, null, "" + pl.getI()));
        elem.appendChild(createElement(doc, X_NAME, null, "" + pl.getX().length()));
        elem.appendChild(createElement(doc, Y_NAME, null, "" + pl.getY().length()));
        
        //Encode the list of attempts.
        ArrayList attempts = pl.getAttempts();
        if(attempts != null && attempts.size() > 0)        
            for(int i = 0; i < attempts.size(); i++)
                elem.appendChild(createElement(doc, ATTEMPT, null, (String)attempts.get(i)));
        
        return doc;
    }

    public String getType() 
    {
        return TYPE;
    }

}
