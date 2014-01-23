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

package io.indy.seni.core.bind;

import java.util.List;

import io.indy.seni.core.Colour;
import io.indy.seni.lang.Binder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeColour;
import io.indy.seni.lang.NodeFn;
import io.indy.seni.lang.NodeList;

public class Core extends Binder {

    // complementary, splitComplementary, analagous, triad

    public static Env bind(Env e) {

        // create a colour node
        e.addBinding(new NodeFn("colour") {
                public Node execute(Env env, List<Node> params)
                    throws LangException {

                    // currently just rgb
                    // TODO: accept hsl etc

                    Binder.checkArityAtLeast(params, 3, keyword());

                    int numArgs = params.size();


                    float r = Node.asFloatValue(params.get(0));
                    float g = Node.asFloatValue(params.get(1));
                    float b = Node.asFloatValue(params.get(2));

                    float a = numArgs == 3 ? 1.0f : Node.asFloatValue(params.get(3));

                    return new NodeColour(Colour.fromRGB(r, g, b, a));
                }
            });

        e.addBinding(new NodeFn("interpolate-lab") {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 3, keyword());

                Colour a = Node.asColourValue(params.get(0));
                Colour b = Node.asColourValue(params.get(1));
                float t = Node.asFloatValue(params.get(2));

                return new NodeColour(Colour.interpolate(a, b, t, Colour.Format.LAB));
            }
        });

        e.addBinding(new NodeFn("interpolate-rgb") {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 3, keyword());

                Colour a = Node.asColourValue(params.get(0));
                Colour b = Node.asColourValue(params.get(1));
                float t = Node.asFloatValue(params.get(2));

                return new NodeColour(Colour.interpolate(a, b, t, Colour.Format.RGB));
            }
        });


        e.addBinding(new NodeFn("interpolate-hsl") {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 3, keyword());

                Colour a = Node.asColourValue(params.get(0));
                Colour b = Node.asColourValue(params.get(1));
                float t = Node.asFloatValue(params.get(2));

                return new NodeColour(Colour.interpolate(a, b, t, Colour.Format.HSL));
            }
        });

        e.addBinding(new NodeFn("interpolate-hsv") {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 3, keyword());

                Colour a = Node.asColourValue(params.get(0));
                Colour b = Node.asColourValue(params.get(1));
                float t = Node.asFloatValue(params.get(2));

                return new NodeColour(Colour.interpolate(a, b, t, Colour.Format.HSV));
            }
        });

        e.addBinding(new NodeFn("complementary") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    Colour c = Node.asColourValue(params.get(0));

                    return new NodeColour(c.complementary());
                }
            });

        e.addBinding(new NodeFn("split-complementary") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    Colour c = Node.asColourValue(params.get(0));
                    Colour cols[] = c.splitComplementary();

                    NodeList res = new NodeList();
                    res.addChild(new NodeColour(c));
                    res.addChild(new NodeColour(cols[0]));
                    res.addChild(new NodeColour(cols[1]));
                    return res;
                }
            });

        e.addBinding(new NodeFn("analagous") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    Colour c = Node.asColourValue(params.get(0));
                    Colour cols[] = c.analagous();

                    NodeList res = new NodeList();
                    res.addChild(new NodeColour(c));
                    res.addChild(new NodeColour(cols[0]));
                    res.addChild(new NodeColour(cols[1]));
                    return res;
                }
            });

        e.addBinding(new NodeFn("triad") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    Binder.checkArity(params, 1, keyword());

                    Colour c = Node.asColourValue(params.get(0));
                    Colour cols[] = c.triad();

                    NodeList res = new NodeList();
                    res.addChild(new NodeColour(c));
                    res.addChild(new NodeColour(cols[0]));
                    res.addChild(new NodeColour(cols[1]));
                    return res;
                }
            });

        return e;
    }
}
