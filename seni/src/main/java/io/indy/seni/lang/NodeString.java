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
}
