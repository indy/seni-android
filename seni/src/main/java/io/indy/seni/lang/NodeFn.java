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

abstract public class NodeFn extends NodeLambda {

    private String mKeyword;

    public NodeFn(String keyword) {
        super(null, null);

        mIsKeyworded = true;
        mKeyword = keyword;
    }

    public String keyword() {
        return mKeyword;
    }

    abstract public Node execute(Env env, List<Node> params);

    public boolean eq(Node n) {
        if (n.mType != mType) {
            return false;
        }

        if (n.mIsKeyworded == false) {
            return false;
        }

        NodeFn nfn = (NodeFn)n;
        return mKeyword.equals(nfn.mKeyword);
    }
}
