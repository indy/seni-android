package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public abstract class NodeLambdaMath extends NodeLambda {

    public NodeLambdaMath() {
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
            return executeInt(((NodeInt)n).getInt(), iter);
        } else if(n.getType() == Node.Type.FLOAT) {
            return executeFloat(((NodeFloat)n).getFloat(), iter);
        } else {
            // throw an error: + only works with int or float
        }

        return null;
    }

    abstract protected Node executeInt(int first, Iterator<Node> iter); 
    abstract protected Node executeFloat(float first, Iterator<Node> iter);
}
