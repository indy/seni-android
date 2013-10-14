package io.indy.seni.lang;

public class NodeString extends Node {

    private String mString;

    public NodeString(String value) {
        super();

        mType = Node.Type.STRING;
        mString = value;
    }

    public String getString() {
        return mString;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.STRING) {
            return false;
        }
        return ((NodeString)n).mString.equals(mString);
    }

}
