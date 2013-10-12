package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaPlus extends NodeLambda {

    public NodeLambdaPlus() {
        super(null, null);
    }

    public Node execute(Env env, List<Node> params) {

        // the params have already been eval'd
        // TODO: only works with INT, change to also work with FLOAT

        int total = 0;

        Iterator<Node> iter = params.iterator();
        Node n;
        while(iter.hasNext()) {
            n = iter.next();
            if(n.getType() != Node.Type.INT) {
                // throw exception
            }
            total += ((NodeInt)n).getInt();
        }

        // evaluate
        return new NodeInt(total);
    }
}
