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

        while(iter.hasNext()) {
            if (first != Node.asFloatValue(iter.next())) {
                return new NodeBoolean(false);
            }
        }

        return new NodeBoolean(true);
    }
}
