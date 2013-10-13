package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaLess extends NodeLambdaMath {

    public NodeLambdaLess() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) {
        
        int less = first;
        int val;
        Node n;

        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.INT) {
                // throw exception
            }
            val = ((NodeInt)n).getInt();

            if (less > val) {
                return new NodeBoolean(false);
            }

            less = val;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) {
        
        float less = first;
        float val;
        Node n;

        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.FLOAT) {
                // throw exception
            }
            val = ((NodeFloat)n).getFloat();

            if (less > val) {
                return new NodeBoolean(false);
            }

            less = val;
        }
        
        return new NodeBoolean(true);
    }
}
