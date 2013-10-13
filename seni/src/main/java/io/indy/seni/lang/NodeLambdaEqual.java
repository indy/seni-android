package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaEqual extends NodeLambdaMath {

    public NodeLambdaEqual() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) {

        while(iter.hasNext()) {
            if (first != Node.asIntValue(iter.next())) {
                return new NodeBoolean(false);
            }
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) {
        
        float val;
        Node n;

        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.FLOAT) {
                // throw exception
            }

            if (first != ((NodeFloat)n).getFloat()) {
                return new NodeBoolean(false);
            }
        }
        
        return new NodeBoolean(true);
    }
}
