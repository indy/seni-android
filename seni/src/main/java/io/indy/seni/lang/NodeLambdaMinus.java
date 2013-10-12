package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMinus extends NodeLambda {

    public NodeLambdaMinus() {
        super(null, null);
    }

    public Node execute(Env env, List<Node> params) {

        // the params have already been eval'd

        Iterator<Node> iter = params.iterator();
        Node n;

        if(iter.hasNext()) {
            n = iter.next();
        } else {
            // throw an error: no args for +

            return null;
        }

        if(n.getType() == Node.Type.INT) {
            return executeInt((NodeInt)n, iter);
        } else if(n.getType() == Node.Type.FLOAT) {
            return executeFloat((NodeFloat)n, iter);
        } else {
            // throw an error: + only works with int or float
        }

        return null;
    }

    private Node executeInt(NodeInt nodeInt, Iterator<Node> iter) {
        
        // iter has already had next() called, it returned n
        
        int total = nodeInt.getInt();
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

    private Node executeFloat(NodeFloat nodeFloat, Iterator<Node> iter) {
        
        // iter has already had next() called, it returned n
        
        float total = nodeFloat.getFloat();
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
