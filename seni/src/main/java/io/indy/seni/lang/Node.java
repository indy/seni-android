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

    public static NodeBoolean asBoolean(Node n) throws LangException {
        if(n.getType() != Type.BOOLEAN) {
            throw new LangException("incompatible cast to BOOLEAN");
        }
        return (NodeBoolean) n;
    }

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
            throw new LangException("cannot cast " + n.getType() +" to NAME");
        }

        return (NodeName) n;
    }

    public static String asNameValue(Node n) throws LangException {
        return asName(n).getName();
    }

    public static NodeList asList(Node n) throws LangException {
        if (n.getType() != Node.Type.LIST) {
            throw new LangException("cannot cast " + n.getType() +" to LIST");
        }

        return (NodeList) n;
    }

    public static NodeLambda asLambda(Node n) throws LangException {
        if (n.getType() != Node.Type.LAMBDA) {
            throw new LangException("cannot cast " + n.getType() +" to LAMBDA");
        }
        return (NodeLambda) n;
    }

    public static NodeLambda resolveLambda(Env env, Node n) 
        throws LangException {
        // n is expected to be either a lambda or a name referencing a lambda
        //
        if (n.getType() == Node.Type.NAME) {
            n = env.lookup(Node.asNameValue(n));
        }
        return Node.asLambda(n);
    }

    public static void debug(Node n, String msg) throws LangException {
        System.out.println(msg);
        debug(n);
    }

    public static void debug(Node n) throws LangException {
        Type t = n.getType();
        if(t == Type.NAME) {
        } else if(t == Type.INT) {
            System.out.println("" + n.getType() + ": " + Node.asIntValue(n));
        } else if(t == Type.FLOAT) {
            System.out.println("" + n.getType() + ": " + Node.asFloatValue(n));
        } else if(t == Type.BOOLEAN) {
            System.out.println("" + n.getType() + ": " + ((NodeBoolean)n).getBoolean());
        } else if(t == Type.LAMBDA) {
            System.out.println("" + n.getType());
        } else if(t == Type.NAME) {
            System.out.println("" + n.getType() + ": " + Node.asNameValue(n));
        } else if(t == Type.STRING) {
            System.out.println("" + n.getType() + ": " + ((NodeString)n).getString());
        } else {
            System.out.println("unknown type to debug");
        }
    }

    public Type getType() {
        return mType;
    }

    abstract public boolean eq(Node n);

}
