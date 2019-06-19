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
 * Representation of a data set
 * Subclass of a legaltag Entity
 * 
 */

public class Dataset extends Entity {
	public Dataset(String n) {
		name = n;
		makeID();
		relations = new ArrayList<Relation>();
	}
}