package io.indy.seni.lang;

abstract public class Node {

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

    public static NodeInt asInt(Node n) throws LangException {
        if(n.getType() != Type.INT) {
            throw new LangException("incompatible cast to INT");
        }
        return (NodeInt) n;
    }

    public static int asIntValue(Node n) throws LangException {
        return asInt(n).getInt();
    }

    public static NodeFloat asFloat(Node n) throws LangException {
        if(n.getType() != Type.FLOAT) {
            throw new LangException("incompatible cast to FLOAT");
        }
        return (NodeFloat) n;
    }

    public static float asFloatValue(Node n) throws LangException {
        return asFloat(n).getFloat();
    }

    public static NodeName asName(Node n) throws LangException {
        if (n.getType() != Node.Type.NAME) {
            throw new LangException("incompatible cast to NAME");
        }

        return (NodeName) n;
    }

    public static String asNameValue(Node n) throws LangException {
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

    public Type getType() {
        return mType;
    }
}
