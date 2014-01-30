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

public class NodeString extends NodeMutate {

    private String mString;

    public NodeString(String value) {
        super();
        init(value);
    }

    public NodeString(String value, boolean alterable) {
        super(alterable);
        init(value);
    }

    private void init(String value) {
        mType = Node.Type.STRING;
        mString = value;
    }

    public NodeMutate mutate() {
        return new NodeString(mString);
    }

    public NodeMutate klone() {
        return kloneSet(new NodeString(mString));
    }

    public String getString() {
        return mString;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.STRING) {
            return false;
        }
        return ((NodeString) n).mString.equals(mString);
    }

    @Override
    protected String scribeValue() throws ScribeException {
        return "\"" + mString + "\"";
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
