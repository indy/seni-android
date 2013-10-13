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

        while(iter.hasNext()) {

            val = Node.asIntValue(iter.next());
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

        while(iter.hasNext()) {

            val = Node.asFloatValue(iter.next());
            if (less > val) {
                return new NodeBoolean(false);
            }
            less = val;
        }

        return new NodeBoolean(true);
    }
}
