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

    public static NodeFloat asFloat(Node n) {
        if(n.getType() != Type.FLOAT) {
            // throw an error
        }
        return (NodeFloat) n;
    }

    public static float asFloatValue(Node n) {
        return asFloat(n).getFloat();
    }

    public static NodeName asName(Node n) {
        if (n.getType() != Node.Type.NAME) {
            // throw an error
        }

        return (NodeName) n;
    }

    public static String asNameValue(Node n) {
        return asName(n).getName();
    }

    public static NodeList asList(Node n) {
        if (n.getType() != Node.Type.LIST) {
            // throw an error
        }

        return (NodeList) n;
    }

    public static NodeLambda asLambda(Node n) {
        if (n.getType() != Node.Type.LAMBDA) {
            // throw an error
        }

        return (NodeLambda) n;
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
