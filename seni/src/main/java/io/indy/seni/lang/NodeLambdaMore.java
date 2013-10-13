package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaMore extends NodeLambdaMath {

    public NodeLambdaMore() {
        super();
    }

    protected Node executeInt(int first, Iterator<Node> iter) {
        
        int more = first;
        int val;
        Node n;

        while(iter.hasNext()) {

            val = Node.asIntValue(iter.next());
            if (more < val) {
                return new NodeBoolean(false);
            }
            more = val;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) {
        
        float more = first;
        float val;
        Node n;

        while(iter.hasNext()) {

            val = Node.asFloatValue(iter.next());
            if (more < val) {
                return new NodeBoolean(false);
            }
            more = val;
        }

        return new NodeBoolean(true);
    }
}
