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

public class BinderMathFns extends Binder {

    public static Env bind(Env e) {

        // TODO: quot, rem, mod, inc, dec, max, min, 

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
