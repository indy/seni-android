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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Env {

    private Env mOuter;
    private HashMap<String, Node> mBindings;

    public static Env bindCoreFuns(Env env) {
        // binds core functions in a new scope of env
        Env e = env.newScope();

        e = bindMathFuns(e);
        e = bindFunctionalFuns(e);
        return e;
    }

    public Env() {
        mOuter = null;
        mBindings = new HashMap<String, Node>();
    }

    public Env(Env outer) {
        mOuter = outer;
        mBindings = new HashMap<String, Node>();
    }

    public Env newScope() {
        return new Env(this);
    }

    public Env addBinding(String key, Node val) {
        mBindings.put(key, val);
        return this;
    }

    public Env addBinding(NodeFn val) {
        return addBinding(val.keyword(), val);
    }

    public boolean hasBinding(String key) {
        return mBindings.containsKey(key);
    }

    public Node lookup(String key) throws LangException {

        Env e = this;
        do {
            if(e.mBindings.containsKey(key)) {
                return e.mBindings.get(key);
            }
            e = e.mOuter;
        } while(e != null);
        
        throw new LangException("unable to find value of key: " + key + " in env");
    }

    private static Env bindFunctionalFuns(Env e) {
        // apply, map, reduce, first, second, nth, cons, concat, mapcat,
        // filter, interleave, interpose, partition, reverse, sort,
        // quot, rem, mod, inc, dec, max, min, count 

        e.addBinding(new NodeFn("map") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    if (params.size() != 2) {
                        String msg = "wrong # of arguments for " + keyword();
                        throw new LangException(msg);
                    }

                    Node fun = params.get(0);
                    if (fun.getType() == Node.Type.NAME) {
                        fun = env.lookup(Node.asNameValue(fun));
                    }
                    NodeLambda lambda = Node.asLambda(fun);

                    NodeList listExpr = Node.asList(params.get(1));

                    NodeList res = new NodeList();
                    for (Node child : listExpr.getChildren()) {
                        res.addChild(lambda.execute(env, child));
                    }
                    return res;
                }
            });

        e.addBinding(new NodeFn("first") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    if (params.size() != 1) {
                        String msg = "wrong # of arguments for " + keyword();
                        throw new LangException(msg);
                    }

                    return Node.asList(params.get(0)).getChild(0);
                }                
            });

        e.addBinding(new NodeFn("second") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    if (params.size() != 1) {
                        String msg = "wrong # of arguments for " + keyword();
                        throw new LangException(msg);
                    }

                    return Node.asList(params.get(0)).getChild(1);
                }                
            });

        e.addBinding(new NodeFn("nth") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    if (params.size() != 2) {
                        String msg = "wrong # of arguments for " + keyword();
                        throw new LangException(msg);
                    }

                    // nth is 0-based
                    int nth = Node.asIntValue(params.get(0));

                    return Node.asList(params.get(1)).getChild(nth);
                }                
            });

        return e;
    }

    private static Env bindMathFuns(Env e) {

        e.addBinding(new NodeFnMath("+") {
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

                    int total = first;
                    try {
                        while(iter.hasNext()) {
                            total += Node.asIntValue(iter.next());
                        }
                    } catch (LangException e) {
                        // log plus
                        throw e;
                    }

                    return new NodeInt(total);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
                    float total = first;
                    try {
                        while(iter.hasNext()) {
                            total += Node.asFloatValue(iter.next());
                        }
                    } catch (LangException e) {
                        throw e;
                    }
                    return new NodeFloat(total);
                }
            });


        e.addBinding(new NodeFnMath("-") {
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

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

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
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
            });

        e.addBinding(new NodeFnMath("/") {
                
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

                    int total = first;
                    try {
                        while(iter.hasNext()) {
                            total /= Node.asIntValue(iter.next());
                        }
                    } catch (LangException e) {
                        throw e;
                    }

                    return new NodeInt(total);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
                    float total = first;
                    try {
                        while(iter.hasNext()) {
                            total /= Node.asFloatValue(iter.next());
                        }
                    } catch (LangException e) {
                        throw e;
                    }
                    return new NodeFloat(total);
                }
            });

        e.addBinding(new NodeFnMath("*") {
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

                    int total = first;
                    try {
                        while(iter.hasNext()) {
                            total *= Node.asIntValue(iter.next());
                        }
                    } catch (LangException e) {
                        throw e;
                    }

                    return new NodeInt(total);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
                    float total = first;
                    try {
                        while(iter.hasNext()) {
                            total *= Node.asFloatValue(iter.next());
                        }
                    } catch (LangException e) {
                        throw e;
                    }
                    return new NodeFloat(total);
                }
            });

        e.addBinding(new NodeFnMath("<") {
                
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

                    int less = first;
                    int val;

                    try {
                        while(iter.hasNext()) {
                            val = Node.asIntValue(iter.next());
                            if (less > val) {
                                return new NodeBoolean(false);
                            }
                            less = val;
                        }

                    } catch (LangException e) {
                        // log less
                        throw e;
                    }
        
                    return new NodeBoolean(true);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
                    float less = first;
                    float val;

                    try {
                        while(iter.hasNext()) {
                            val = Node.asFloatValue(iter.next());
                            if (less > val) {
                                return new NodeBoolean(false);
                            }
                            less = val;
                        }
                    } catch (LangException e) {
                        throw e;
                    }

                    return new NodeBoolean(true);

                }
            });

        e.addBinding(new NodeFnMath(">") {
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {

                    int less = first;
                    int val;

                    try {
                        while(iter.hasNext()) {
                            val = Node.asIntValue(iter.next());
                            if (less < val) {
                                return new NodeBoolean(false);
                            }
                            less = val;
                        }

                    } catch (LangException e) {
                        // log less
                        throw e;
                    }
        
                    return new NodeBoolean(true);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
        
                    float less = first;
                    float val;

                    try {
                        while(iter.hasNext()) {
                            val = Node.asFloatValue(iter.next());
                            if (less < val) {
                                return new NodeBoolean(false);
                            }
                            less = val;
                        }
                    } catch (LangException e) {
                        throw e;
                    }

                    return new NodeBoolean(true);
                }
            });

        e.addBinding(new NodeFnMath("=") {
                protected Node executeInt(int first, Iterator<Node> iter) 
                    throws LangException {
                    try {
                        while(iter.hasNext()) {
                            if (first != Node.asIntValue(iter.next())) {
                                return new NodeBoolean(false);
                            }
                        }
                    } catch (LangException e) {
                        // log equal
                        throw e;
                    }
        
                    return new NodeBoolean(true);
                }

                protected Node executeFloat(float first, Iterator<Node> iter)
                    throws LangException {
                    try {
                        while(iter.hasNext()) {
                            if (first != Node.asFloatValue(iter.next())) {
                                return new NodeBoolean(false);
                            }
                        }
                    } catch (LangException e) {
                        throw e;
                    }

                    return new NodeBoolean(true);        
                }
            });

        return e;
    }
}
