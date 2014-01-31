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

    private boolean mHasSpecifiedRange;
    private float mMinRange;
    private float mMaxRange;

    public NodeFloat(float value) {
        this(value, false);
    }

    public NodeFloat(float value, boolean alterable) {
        super(alterable);

        mType = Node.Type.FLOAT;
        mFloat = value;

        mHasSpecifiedRange = false;
        mMinRange = 0.0f;
        mMaxRange = 1.0f;
    }

    public NodeMutate mutate() {
        float f = (float) Math.random() * (mMaxRange - mMinRange);
        float val = f + mMinRange;

        return kloneWithValue(val);
    }

    public NodeMutate klone() {
        return kloneWithValue(mFloat);
    }

    private NodeFloat kloneWithValue(float val) {
        NodeFloat n = new NodeFloat(val, mAlterable);

        n.mGenSym = mGenSym;

        n.mHasSpecifiedRange = this.mHasSpecifiedRange;
        n.mMinRange = this.mMinRange;
        n.mMaxRange = this.mMaxRange;

        return n;
    }

    @Override
    public void addParameterNode(Node node) {
        super.addParameterNode(node);

        // todo:
        // an env in which the parameter functions evaluate to themselves
        // call Interpreter:eval
        // use the result

        // BUT...
        // in the meantime just manually parse the node children

        try {
            NodeList nodeList = Node.asList(node);
            String name = Node.asNameValue(nodeList.getChild(0));
            if (name.equals(NodeMutate.IN_RANGE)) {
                mHasSpecifiedRange = true;
                mMinRange = Node.asFloatValue(nodeList.getChild(1));
                mMaxRange = Node.asFloatValue(nodeList.getChild(2));
            }
        } catch (LangException e) {
            e.printStackTrace();
        }
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
        String ret = String.valueOf(mFloat);
        if(mHasSpecifiedRange) {
            ret += " (" + NodeMutate.IN_RANGE +
                    " " + String.valueOf(mMinRange) +
                    " " + String.valueOf(mMaxRange) +
                    ")";
        }
        return ret;
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
