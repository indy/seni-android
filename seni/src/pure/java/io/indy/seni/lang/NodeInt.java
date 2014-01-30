/*
 * Copyright 2014 Inderjit Gill
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

public class NodeInt extends NodeMutate {

    private int mInteger;

    public NodeInt(int value) {
        this(value, false);
    }

    public NodeInt(int value, boolean alterable) {
        super(alterable);

        mType = Node.Type.INT;
        mInteger = value;
    }

    public NodeMutate mutate() {
        int val = (int)(((float)Math.random()) * 100.0f);
        return kloneSet(new NodeInt(val));
    }

    public NodeMutate klone() {
        return kloneSet(new NodeInt(mInteger));
    }

    public int getInt() {
        return mInteger;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.INT) {
            return false;
        }
        return mInteger == ((NodeInt) n).mInteger;
    }

    @Override
    protected String scribeValue() throws ScribeException {
        return Integer.toString(mInteger);
    }

    public String toString() {
        try {
            String value = "";
            if (isAlterable()) {
                value = "[" + scribeValue() + "]";
            } else {
                value = "" + scribeValue();
            }
            return "" + getType() + ": " + value;
        } catch (ScribeException e) {
            e.printStackTrace();
        }
        return "" + getType() + ": toString ERROR";
    }
}
