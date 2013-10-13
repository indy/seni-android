package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMinus extends NodeLambdaMath {

    public NodeLambdaMinus() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {
        
        int total = first;
        try {
            while(iter.hasNext()) {
                total -= Node.asIntValue(iter.next());
            }
        } catch (LangException e) {
            // log minus
            throw e;
        }

        return new NodeInt(total);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {
        
        float total = first;
        try {
            while(iter.hasNext()) {
                total -= Node.asFloatValue(iter.next());
            }
        } catch (LangException e) {
            throw e;
        }
        
        return new NodeFloat(total);
    }
}
