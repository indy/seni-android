package io.indy.seni.lang;

abstract public class Node {

    public enum Type {
        LIST,
        INT,
        FLOAT,
        NAME,
        STRING
    }

    protected Node mParent;
    protected Type mType;

    public Node(Node parent) {
        mParent = parent;
    }

    public Type getType() {
        return mType;
    }
}
