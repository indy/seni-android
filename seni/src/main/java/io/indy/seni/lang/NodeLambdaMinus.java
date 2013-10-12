package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMinus extends NodeLambdaMath {

    public NodeLambdaMinus() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) {
        
        int total = first;
        Node n;

        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.INT) {
                // throw exception
            }
            total -= ((NodeInt)n).getInt();
        }
        
        return new NodeInt(total);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) {
        
        float total = first;
        Node n;

        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.FLOAT) {
                // throw exception
            }
            total -= ((NodeFloat)n).getFloat();
        }
        
        return new NodeFloat(total);
    }
}
