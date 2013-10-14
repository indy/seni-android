package io.indy.seni.lang;

public class NodeName extends Node {

    private String mName;

    public NodeName(String value) {
        super();

        mType = Node.Type.NAME;
        mName = value;
    }

    public String getName() {
        return mName;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.NAME) {
            return false;
        }
        return ((NodeName)n).mName.equals(mName);
    }
}
