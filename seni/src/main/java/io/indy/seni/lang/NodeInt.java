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

public class NodeInt extends Node {

    private int mInteger;

    public NodeInt(int value) {
        super();
        init(value);
    }

    public NodeInt(int value, boolean alterable) {
        super(alterable);
        init(value);
    }

    private void init(int value) {
        mType = Node.Type.INT;
        mInteger = value;
    }

    public int getInt() {
        return mInteger;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.INT) {
            return false;
        }
        return mInteger == ((NodeInt)n).mInteger;
    }

    protected String scribeValue() throws ScribeException {
        return Integer.toString(mInteger);
    }

    public String toString() {
        try {
            return "" + getType() + ": " + scribe();
        } catch (ScribeException e) {
            e.printStackTrace();
        }
        return "" + getType() + ": toString ERROR";
    }

}
