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

package io.indy.seni.runtime.bind;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Binder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeList;
import io.indy.seni.runtime.CoreBridge;
import io.indy.seni.runtime.NodeSeniContext;
import io.indy.seni.runtime.NodeSpecialSeniContext;
import io.indy.seni.runtime.SeniContext;

public class Platform extends Binder {

    private static final String TAG = "Platform";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static Env bind(Env e, SeniContext sc) {

        e.addBinding(new NodeSpecialSeniContext("scope", sc) {
            public Node executeSpecial(Env env, NodeList listExpr)
                    throws LangException {

                mSeniContext.getCanvas().save();

                List<Node> children = listExpr.getChildren();
                Iterator<Node> iter = children.iterator();
                iter.next(); // 'scope' name
                while (iter.hasNext()) {
                    Interpreter.eval(env, iter.next());
                }

                mSeniContext.getCanvas().restore();

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("set-colour", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 1, keyword());

                CoreBridge.setColour(mSeniContext.getPaint(),
                        Node.asColourValue(params.get(0)));

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("rotate", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 1, keyword());

                float angle = Node.asFloatValue(params.get(0));

                mSeniContext.getCanvas().rotate(angle);

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("translate", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 2, keyword());

                float x = Node.asFloatValue(params.get(0));
                float y = Node.asFloatValue(params.get(1));

                mSeniContext.getCanvas().translate(x, y);

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("scale", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 2, keyword());

                float sx = Node.asFloatValue(params.get(0));
                float sy = Node.asFloatValue(params.get(1));

                mSeniContext.getCanvas().scale(sx, sy);

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("rect", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 4, keyword());

                float left = Node.asFloatValue(params.get(0));
                float top = Node.asFloatValue(params.get(1));
                float right = Node.asFloatValue(params.get(2));
                float bottom = Node.asFloatValue(params.get(3));

                mSeniContext.getCanvas().drawRect(left, top, right, bottom,
                        mSeniContext.getPaint());

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("circle", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 3, keyword());

                float x = Node.asFloatValue(params.get(0));
                float y = Node.asFloatValue(params.get(1));
                float r = Node.asFloatValue(params.get(2));

                mSeniContext.getCanvas().drawCircle(x, y, r, mSeniContext.getPaint());

                return Interpreter.NODE_NULL;
            }
        });

        e.addBinding(new NodeSeniContext("triangle", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 6, keyword());

                float p1x = Node.asFloatValue(params.get(0));
                float p1y = Node.asFloatValue(params.get(1));
                float p2x = Node.asFloatValue(params.get(2));
                float p2y = Node.asFloatValue(params.get(3));
                float p3x = Node.asFloatValue(params.get(4));
                float p3y = Node.asFloatValue(params.get(5));

                Canvas canvas = mSeniContext.getCanvas();
                Paint paint = mSeniContext.getPaint();
                Path path = mSeniContext.getPath();

                path.reset();
                path.setFillType(Path.FillType.EVEN_ODD);
                path.moveTo(p1x, p1y);
                path.lineTo(p2x, p2y);
                path.lineTo(p3x, p3y);
                path.close();

                canvas.drawPath(path, paint);

                return Interpreter.NODE_NULL;
            }
        });

        return e;
    }
}
