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

public class NodeBoolean extends NodeMutate {

    private boolean mBoolean;

    public NodeBoolean(boolean value) {
        this(value, false);
    }

    public NodeBoolean(boolean value, boolean alterable) {
        super(alterable);

        mType = Node.Type.BOOLEAN;
        mBoolean = value;
    }

    public NodeMutate mutate() {
        boolean val = Math.random() < 0.5;

        return kloneWithValue(val);
    }

    public NodeMutate klone() {
        return kloneWithValue(mBoolean);
    }

    public NodeMutate deviate(String val) {
        boolean v = val.equals("true");
        return kloneWithValue(v);
    }

    private NodeBoolean kloneWithValue(boolean val) {
        NodeBoolean n = new NodeBoolean(val, mAlterable);

        n.mGenSym = mGenSym;

        return n;
    }


    public boolean getBoolean() {
        return mBoolean;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.BOOLEAN) {
            return false;
        }
        return mBoolean == ((NodeBoolean) n).mBoolean;
    }

    @Override
    protected String scribeValue() throws ScribeException {
        return mBoolean ? "true" : "false";
    }

    public String toString() {
        return "" + getType() + ": " + (mBoolean ? "true" : "false");
    }
}
