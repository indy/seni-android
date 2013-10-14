package io.indy.seni.lang;

public class NodeFloat extends Node {

    private float mFloat;

    public NodeFloat(float value) {
        super();

        mType = Node.Type.FLOAT;
        mFloat = value;
    }

    public float getFloat() {
        return mFloat;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.FLOAT) {
            return false;
        }
        return mFloat == ((NodeFloat)n).mFloat;
    }

}
