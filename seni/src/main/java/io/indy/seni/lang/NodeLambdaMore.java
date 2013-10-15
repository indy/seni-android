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

public class NodeLambdaMore extends NodeLambdaMath {

    public NodeLambdaMore() {
        super();

        mHasCompareSymbol = true;
        mCompareSymbol = ">";
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {
        
        int more = first;
        int val;
        Node n;

        try {
            while(iter.hasNext()) {
                val = Node.asIntValue(iter.next());
                if (more < val) {
                    return new NodeBoolean(false);
                }
                more = val;
            }
        } catch (LangException e) {
            // log more
            throw e;
        }
        
        return new NodeBoolean(true);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {
        
        float more = first;
        float val;
        Node n;

        try {
            while(iter.hasNext()) {
                val = Node.asFloatValue(iter.next());
                if (more < val) {
                    return new NodeBoolean(false);
                }
                more = val;
            }
        } catch (LangException e) {
            throw e;
        }

        return new NodeBoolean(true);
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.LAMBDA) {
            return false;
        }
        return mHasCompareSymbol && mCompareSymbol.equals(">");
    }

}
