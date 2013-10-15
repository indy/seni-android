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

public class NodeLambdaMinus extends NodeLambdaMath {

    public NodeLambdaMinus() {
        super();

        mHasCompareSymbol = true;
        mCompareSymbol = "-";
    }

    protected Node executeInt(int first, Iterator<Node> iter) throws LangException {
        
        int total = first;
        try {
            while(iter.hasNext()) {
                total -= Node.asIntValue(iter.next());
            }
        } catch (LangException e) {
            // log minus
            throw e;
        }

        return new NodeInt(total);
    }

    protected Node executeFloat(float first, Iterator<Node> iter) throws LangException {
        
        float total = first;
        try {
            while(iter.hasNext()) {
                total -= Node.asFloatValue(iter.next());
            }
        } catch (LangException e) {
            throw e;
        }
        
        return new NodeFloat(total);
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.LAMBDA) {
            return false;
        }
        return mHasCompareSymbol && mCompareSymbol.equals("-");
    }
}
