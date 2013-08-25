package io.indy.seni.lang;

public class Token {

    public enum Type {
        LIST_START,
        LIST_END,
        // COMMENT, // remove comments
        INT,
        FLOAT,
        NAME,
        // KEYWORD, // too clojure specific???
        STRING
    }

    public static class TokenException extends Exception {
        public TokenException(String message){
            super(message);
        }
    }

    private Type mType;

    private String mStringValue;
    private int mIntValue;
    private float mFloatValue;

    public Token(Type type) {
        mType = type;
    }

    public Token(Type type, String value) {
        mType = type;
        mStringValue = value;
    }

    public Token(Type type, int value) throws TokenException {
        if(type != Type.INT) {
            throw new TokenException("value: " + value + " expected to have type INT");
        }
        mType = Type.INT;
        mIntValue = value;
    }

    public Token(Type type, float value) throws TokenException {
        if(type != Type.FLOAT) {
            throw new TokenException("value: " + value + " expected to have type FLOAT");
        }
        mType = Type.FLOAT;
        mFloatValue = value;
    }

    public Type getType() {
        return mType;
    }

    public String getStringValue() {
        return mStringValue;
    }

    public int getIntValue() {
        return mIntValue;
    }

    public float getFloatValue() {
        return mFloatValue;
    }
}
