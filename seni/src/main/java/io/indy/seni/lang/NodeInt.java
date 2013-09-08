package io.indy.seni.lang;

public class NodeInt extends Node {

    private int mInteger;

    public NodeInt(Node parent, int value) {
        super(parent);

        mType = Node.Type.INT;
        mInteger = value;
    }

    public int getInt() {
        return mInteger;
    }
}
