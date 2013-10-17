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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class BinderFunctionalFns extends Binder {

    public static Env bind(Env e) {
        // todo:
        // apply, reduce, 

        e.addBinding(new NodeFn("map") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArgs(params, 2, keyword());

                    NodeLambda fn = Node.resolveLambda(env, params.get(0));

                    NodeList listExpr = Node.asList(params.get(1));

                    NodeList res = new NodeList();
                    for (Node child : listExpr.getChildren()) {
                        res.addChild(fn.execute(env, child));
                    }
                    return res;
                }
            });

        return e;
    }    
}
