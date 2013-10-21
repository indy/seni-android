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
        // filter, interleave, interpose, partition, reverse, sort 

        e.addBinding(new NodeFn("first") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    NodeList nodeList = Node.asList(params.get(0));

                    return nodeList.getChild(0);
                }                
            });

        e.addBinding(new NodeFn("second") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    NodeList nodeList = Node.asList(params.get(0));

                    return nodeList.getChild(1);
                }                
            });

        e.addBinding(new NodeFn("nth") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 2, keyword());

                    // nth is 0-based
                    int nth = Node.asIntValue(params.get(0));
                    NodeList nodeList = Node.asList(params.get(1));

                    return nodeList.getChild(nth);
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

        e.addBinding(new NodeFn("concat") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    NodeList res = new NodeList();
                    NodeList nodeList;

                    for (Node n : params) {
                        // all params have to be lists
                        try {
                            nodeList = Node.asList(n);
                        } catch (LangException e) {
                            String msg = "All args to concat have to be lists";
                            throw new LangException(msg);
                        }

                        for(Node m : nodeList.getChildren()) {
                            res.addChild(m);
                        }
                    }
                    return res;
                }                
            });

        e.addBinding(new NodeFn("count") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    NodeList nodeList = Node.asList(params.get(0));

                    return new NodeInt(nodeList.size());
                }                
            });


        e.addBinding(new NodeFn("mapcat") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    // first arg is a lambda|name rest are lists
                    NodeLambda fn = Node.resolveLambda(env, params.get(0));

                    int numArgs = params.size() - 1;
                    List<Node> fnArgs = new ArrayList<Node>(numArgs);

                    NodeList firstList = Node.asList(params.get(1));
                    int numIterations = firstList.size();

                    List<Node> resLists = new ArrayList<Node>(numIterations);

                    for (int i=0;i<numIterations;i++) {
                        fnArgs.clear();
                        for (int j=1;j<params.size();j++) {
                            NodeList nl = Node.asList(params.get(j));
                            fnArgs.add(nl.getChildren().get(i));
                        }
                        // invoking the lambda returns a list
                        Node res = fn.execute(env, fnArgs);
                        resLists.add(res);
                    }

                    // concat the returned lists
                    NodeList ret = new NodeList();
                    NodeList nodeList;

                    for (Node n : resLists) {
                        // all params have to be lists
                        try {
                            nodeList = Node.asList(n);
                        } catch (LangException e) {
                            String msg = "All results have to be lists";
                            throw new LangException(msg);
                        }

                        for(Node m : nodeList.getChildren()) {
                            ret.addChild(m);
                        }
                    }

                    return ret;
                }                
            });

        e.addBinding(new NodeFn("reverse") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());
                    
                    NodeList res = new NodeList();

                    NodeList in = Node.asList(params.get(0));
                    int inSize = in.size();

                    for (int i=0;i<inSize;i++) {
                        res.addChild(in.getChild(inSize - i - 1));
                    }

                    return res;
                }                
            });


        return e;
    }    
}
