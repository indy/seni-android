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

public class NodeSpecial extends Node {

    private String mKeyword;

    public NodeSpecial(String keyword) {
        super();

        Interpreter.registerSpecial(keyword);

        mKeyword = keyword;

        mType = Type.SPECIAL;
    }

    public Node executeSpecial(Env env, NodeList listExpr) throws LangException {

        // nothing has been eval'd


        return null;//Interpreter.eval(scoped, mBody);
    }

    public String keyword() {
        return mKeyword;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.SPECIAL) {
            return false;
        }
        return true; // todo: fix this
    }
}
