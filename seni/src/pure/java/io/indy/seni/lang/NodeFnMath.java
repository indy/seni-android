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

import java.util.Iterator;
import java.util.List;

public abstract class NodeFnMath extends NodeFn {

    public NodeFnMath(String keyword) {
        super(keyword);
    }

    public Node execute(Env env, List<Node> params) {

        // the params have already been eval'd

        Iterator<Node> iter = params.iterator();
        Node n;

        if (iter.hasNext()) {
            n = iter.next();
        } else {
            // throw an error: no args for +
            return null;
        }

        try {

            if (n.getType() == Node.Type.INT) {
                return executeInt(Node.asIntValue(n), iter);
            } else if (n.getType() == Node.Type.FLOAT) {
                return executeFloat(Node.asFloatValue(n), iter);
            } else {
                // throw an error: + only works with int or float
            }

        } catch (LangException e) {
            // log e
        }

        return null;
    }

    abstract protected Node executeInt(int first, Iterator<Node> iter) throws LangException;

    abstract protected Node executeFloat(float first, Iterator<Node> iter) throws LangException;
}
