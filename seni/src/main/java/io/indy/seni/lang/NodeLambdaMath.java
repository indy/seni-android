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

        try {

            if(n.getType() == Node.Type.INT) {
                return executeInt(Node.asIntValue(n), iter);
            } else if(n.getType() == Node.Type.FLOAT) {
                return executeFloat(Node.asFloatValue(n), iter);
            } else {
                // throw an error: + only works with int or float
            }

        } catch (LangException e) {
            // log e
        }

        return null;
    }

    abstract protected Node executeInt(int first, Iterator<Node> iter) throws LangException; 
    abstract protected Node executeFloat(float first, Iterator<Node> iter) throws LangException;
}
