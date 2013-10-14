package io.indy.seni.lang;

public class NodeBoolean extends Node {

    private boolean mBoolean;

    public NodeBoolean(boolean value) {
        super();

        mType = Node.Type.BOOLEAN;
        mBoolean = value;
    }

    public boolean getBoolean() {
        return mBoolean;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.BOOLEAN) {
            return false;
        }
        return mBoolean == ((NodeBoolean)n).mBoolean;
    }
}
