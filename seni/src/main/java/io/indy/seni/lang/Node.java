package io.indy.seni.lang;

abstract public class Node {

    public static NodeInt asInt(Node n) {
        if(n.getType() != Type.INT) {
            // throw an error
        }
        return (NodeInt) n;
    }

    public static int asIntValue(Node n) {
        return asInt(n).getInt();
    }

    public enum Type {
        LIST,
        INT,
        FLOAT,
        NAME,
        STRING,
        BOOLEAN,
        LAMBDA,
        NULL
    }

    protected Type mType;

    public Node() {
    }

    public Type getType() {
        return mType;
    }
}
