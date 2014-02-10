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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NodeName extends NodeMutate {

    protected static String IN_SET = "in-set";

    private String mName;

    private boolean mHasParameterSet;
    private Set<String> mParameterSet;

    public NodeName(String value) {
        this(value, false);
    }

    public NodeName(String value, boolean alterable) {
        super(alterable);

        mType = Node.Type.NAME;
        mName = value;

        mHasParameterSet = false;
    }

    public NodeMutate mutate() {
        // todo: test mutate

        int size = mParameterSet.size();
        float f = (float) Math.random() * size;
        int index = (int) f;

        int count = 0;
        for (String s : mParameterSet) {
            if (count == index) {
                return kloneWithValue(s);
            }
            count++;
        }

        // NOTE: should never get here
        return kloneWithValue(mName);
    }

    public NodeMutate klone() {
        return kloneWithValue(mName);
    }

    public NodeMutate deviate(String val) {
        return kloneWithValue(val);
    }

    public String asSerialisableString() {
        return mName;
    }


    private NodeName kloneWithValue(String val) {
        NodeName n = new NodeName(val, mAlterable);

        n.mGenSym = mGenSym;

        n.mHasParameterSet = this.mHasParameterSet;
        if(mHasParameterSet) {
            n.mParameterSet = new HashSet<String>();

            for(String s : mParameterSet) {
                n.mParameterSet.add(s);
            }
        }

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
            if (name.equals(IN_SET)) {

                mParameterSet = new HashSet<String>();

                List<Node> children = nodeList.getChildren();
                for (int i = 1; i < children.size(); i++) {
                    mParameterSet.add(Node.asNameValue(children.get(i)));
                }
            }
        } catch (LangException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return mName;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.NAME) {
            return false;
        }
        return ((NodeName) n).mName.equals(mName);
    }

    @Override
    protected String scribeValue() throws ScribeException {
        String ret = asSerialisableString();
        if(mHasParameterSet) {
            ret += " (" + IN_SET + " ";
            for(String s : mParameterSet) {
                ret += " " + s;
            }
            ret += ")";
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
