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

public class NodeLambda extends Node {

    protected List<String> mArgs;
    protected Node mBody;
    protected boolean mIsKeyworded;

    public NodeLambda(List<String> args, Node body) {
        super();

        mType = Node.Type.LAMBDA;
        mArgs = args;
        mBody = body;

        // true if we have specified a keyword for this lambda
        // (currently only true for objects from NodeFn derived classes)
        mIsKeyworded = false;
    }

    public List<String> getArgs() {
        return mArgs;
    }

    public Node getBody() {
        return mBody;
    }

    public Node execute(Env env, List<Node> params) throws LangException {

        // the params have already been eval'd

        if(mArgs.size() != params.size()) {
            // throw an exception
        }

        // bind params to a new scope
        Env scoped = bindParams(env.newScope(), params);

        // evaluate
        return Interpreter.eval(scoped, mBody);
    }

    protected Env bindParams(Env env, List<Node> params) {
        // bind params to mArgs
        Iterator<String> argIter = mArgs.iterator();
        Iterator<Node> paramIter = params.iterator();
        while(argIter.hasNext()) {
            env.addBinding(argIter.next(), paramIter.next());
        }

        return env;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.LAMBDA) {
            return false;
        }
        return true; // todo: fix this
    }
}
