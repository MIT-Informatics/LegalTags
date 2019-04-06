package legaltags.expert.gui;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.JIPVariable;
import java.util.List;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.function.Function;
/* 
 * Java representation of a relation in the Prolog formalization
 * 
 */

public class Relation {
	String predicate;
	List<String> args;
	public String toString () {
		return predicate + "(" + String.join(",", args) + ")";
	}
}