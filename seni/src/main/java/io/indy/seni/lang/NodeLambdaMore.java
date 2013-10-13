package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMore extends NodeLambdaMath {

    public NodeLambdaMore() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {
        
        int more = first;
        int val;
        Node n;

        try {
            while(iter.hasNext()) {
                val = Node.asIntValue(iter.next());
                if (more < val) {
                    return new NodeBoolean(false);
                }
                more = val;
            }
        } catch (LangException e) {
            // log more
            throw e;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {
        
        float more = first;
        float val;
        Node n;

        try {
            while(iter.hasNext()) {
                val = Node.asFloatValue(iter.next());
                if (more < val) {
                    return new NodeBoolean(false);
                }
                more = val;
            }
        } catch (LangException e) {
            throw e;
        }

        return new NodeBoolean(true);
    }
}
