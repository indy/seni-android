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

public class NodeFloat extends NodeMutate {

    private float mFloat;

    public NodeFloat(float value) {
        super();
        init(value);
    }

    public NodeFloat(float value, boolean alterable) {
        super(alterable);
        init(value);
    }

    private void init(float value) {
        mType = Node.Type.FLOAT;
        mFloat = value;
    }

    public NodeMutate mutate() {
        float val = (float) Math.random();
        return kloneSet(new NodeFloat(val));
    }

    public NodeMutate klone() {
        return kloneSet(new NodeFloat(mFloat));
    }

    public float getFloat() {
        return mFloat;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.FLOAT) {
            return false;
        }
        return mFloat == ((NodeFloat) n).mFloat;
    }

    @Override
    protected String scribeValue() throws ScribeException {
        return String.valueOf(mFloat);
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
