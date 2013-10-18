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

public class BinderListFns extends Binder {

    public static Env bind(Env e) {
        // todo:
        // cons, concat, mapcat, filter, interleave, interpose, partition, reverse, sort, count 

        e.addBinding(new NodeFn("first") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    return Node.asList(params.get(0)).getChild(0);
                }                
            });

        e.addBinding(new NodeFn("second") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    return Node.asList(params.get(0)).getChild(1);
                }                
            });

        e.addBinding(new NodeFn("nth") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 2, keyword());

                    // nth is 0-based
                    int nth = Node.asIntValue(params.get(0));

                    return Node.asList(params.get(1)).getChild(nth);
                }                
            });

        e.addBinding(new NodeFn("cons") {
                // (cons 2 (quote (4 8)))
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 2, keyword());
                    
                    NodeList res = new NodeList();

                    //Node.debug(params.get(1), "should be a list");

                    res.addChild(params.get(0));
                    for (Node child : Node.asList(params.get(1)).getChildren()) {
                        res.addChild(child);
                    }

                    return res;
                }                
            });

        return e;
    }    
}
