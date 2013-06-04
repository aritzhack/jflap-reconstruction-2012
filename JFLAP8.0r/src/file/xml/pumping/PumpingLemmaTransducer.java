package file.xml.pumping;

import model.pumping.PumpingLemma;
import file.xml.StructureTransducer;


public abstract class PumpingLemmaTransducer extends StructureTransducer<PumpingLemma>
{
    /**
     * The tag for the name of the pumping lemma.
     */
    public static String LEMMA_NAME = "name";
    /**
     * The tag for who goes first.
     */
    public static String FIRST_PLAYER = "first_player";
    /**
     * The tag for the <i>m</i> value of the pumping lemma.
     */
    public static String M_NAME = "m";
    /**
     * The tag for the <i>w</i> value of the pumping lemma.
     */
    public static String W_NAME = "w";
    /**
     * The tag for the <i>i</i> value of the pumping lemma.
     */
    public static String I_NAME = "i";
    /**
     * The tag for a representation of a prior attempt.
     */
    public static String ATTEMPT = "attempt";
    /**
     * The comment for <i>m</i>.
     */
    public static String COMMENT_M = "The user's input of m.";
    /**
     * The comment for <i>i</i>. The value of <i>i</i> is needed because
     * it is sometimes randomized.
     */
    public static String COMMENT_I = "The program's value of i.";
}
