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

        e.addBinding(new NodeFn("apply") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArgs(params, 2, keyword());

                    NodeLambda fn = Node.resolveLambda(env, params.get(0));
                    NodeList listExpr = Node.asList(params.get(1));
                    List<Node> fnArgs = listExpr.getChildren();

                    return fn.execute(env, fnArgs);
                }
            });

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

        e.addBinding(new NodeFn("reduce") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    int numArgs = params.size();
                    if (numArgs < 2 || numArgs > 3) {
                        String msg = "wrong # of arguments for " + keyword();
                        throw new LangException(msg);
                    }

                    NodeList coll;
                    Iterator<Node> iter;
                    Node total;

                    if (numArgs == 2) {
                        coll = Node.asList(params.get(1));
                        iter = coll.getChildren().iterator();
                        if (iter.hasNext()) {
                            total = iter.next();
                        } else {
                            throw new LangException("empty list given to reduce");
                        }
                    } else {
                        coll = Node.asList(params.get(2));
                        iter = coll.getChildren().iterator();
                        total = params.get(1);
                    }

                    NodeLambda fn = Node.resolveLambda(env, params.get(0));

                    List<Node> args = new ArrayList<Node>();

                    while (iter.hasNext()) {
                        args.clear();
                        args.add(total);
                        args.add(iter.next());
                        total = fn.execute(env, args);
                    }

                    return total;
                }
            });

        return e;
    }    
}
