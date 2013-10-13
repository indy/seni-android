package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMultiply extends NodeLambdaMath {

    public NodeLambdaMultiply() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) {
        
        int total = first;
        while(iter.hasNext()) {
            total *= Node.asIntValue(iter.next());
        }
        
        return new NodeInt(total);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) {
        
        float total = first;
        while(iter.hasNext()) {
            total *= Node.asFloatValue(iter.next());
        }
        
        return new NodeFloat(total);
    }
}
