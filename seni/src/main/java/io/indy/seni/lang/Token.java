/*
 * Copyright 2013 Inderjit Gill
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.indy.seni.lang;

public class Token {

    public enum Type {
        UNKNOWN,
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
