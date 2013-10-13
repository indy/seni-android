package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaLess extends NodeLambdaMath {

    public NodeLambdaLess() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {
        
        int less = first;
        int val;

        try {
            while(iter.hasNext()) {
                val = Node.asIntValue(iter.next());
                if (less > val) {
                    return new NodeBoolean(false);
                }
                less = val;
            }

        } catch (LangException e) {
            // log less
            throw e;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {
        
        float less = first;
        float val;

        try {
            while(iter.hasNext()) {
                val = Node.asFloatValue(iter.next());
                if (less > val) {
                    return new NodeBoolean(false);
                }
                less = val;
            }
        } catch (LangException e) {
            throw e;
        }

        return new NodeBoolean(true);
    }
}
