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

    public static NodeLambda resolveLambda(Env env, Node n) 
        throws LangException {
        // n is expected to be either a lambda or a name referencing a lambda
        //
        if (n.getType() == Node.Type.NAME) {
            n = env.lookup(Node.asNameValue(n));
        }
        return Node.asLambda(n);
    }

    public Type getType() {
        return mType;
    }

    abstract public boolean eq(Node n);

}
