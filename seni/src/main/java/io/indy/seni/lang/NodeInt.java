package io.indy.seni.lang;

public class NodeInt extends Node {

    private int mInteger;

    public NodeInt(int value) {
        super();

        mType = Node.Type.INT;
        mInteger = value;
    }

    public int getInt() {
        return mInteger;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.INT) {
            return false;
        }
        return mInteger == ((NodeInt)n).mInteger;
    }

}
