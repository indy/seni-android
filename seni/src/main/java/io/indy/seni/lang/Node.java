package io.indy.seni.lang;

abstract public class Node {

    public enum Type {
        LIST,
        INT,
        FLOAT,
        NAME,
        STRING
    }

    protected Type mType;

    public Node() {
    }

    public Type getType() {
        return mType;
    }
}
