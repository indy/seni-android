package io.indy.seni.lang;

import java.util.Iterator;
import java.util.List;

public class NodeLambdaEqual extends NodeLambdaMath {

    public NodeLambdaEqual() {
        super();

        mHasCompareSymbol = true;
        mCompareSymbol = "=";
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {

        try {
            while(iter.hasNext()) {
                if (first != Node.asIntValue(iter.next())) {
                    return new NodeBoolean(false);
                }
            }
        } catch (LangException e) {
            // log equal
            throw e;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {

        try {
            while(iter.hasNext()) {
                if (first != Node.asFloatValue(iter.next())) {
                    return new NodeBoolean(false);
                }
            }
        } catch (LangException e) {
            throw e;
        }

        return new NodeBoolean(true);
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.LAMBDA) {
            return false;
        }
        return mHasCompareSymbol && mCompareSymbol.equals("=");
    }

}
