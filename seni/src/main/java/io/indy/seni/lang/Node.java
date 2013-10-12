package io.indy.seni.lang;

abstract public class Node {

    public enum Type {
        LIST,
        INT,
        FLOAT,
        NAME,
        STRING,
        BOOLEAN,
        NULL
    }

    protected Type mType;

    public Node() {
    }

    public Type getType() {
        return mType;
    }
}
