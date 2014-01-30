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

public class NodeName extends NodeMutate {

    private String mName;

    public NodeName(String value) {
        super();
        init(value);
    }

    public NodeName(String value, boolean alterable) {
        super(alterable);
        init(value);
    }

    private void init(String value) {
        mType = Node.Type.NAME;
        mName = value;
    }

    public NodeMutate mutate() {
        return kloneSet(new NodeName(mName));
    }

    public NodeMutate klone() {
        return kloneSet(new NodeName(mName));
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
        return mName;
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
