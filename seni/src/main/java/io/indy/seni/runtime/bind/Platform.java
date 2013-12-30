package io.indy.seni.runtime.bind;

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
import io.indy.seni.lang.NodeNull;
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
                while(iter.hasNext()) {
                    Interpreter.eval(env, iter.next());
                }

                mSeniContext.getCanvas().restore();

                return new NodeNull(); // todo: replace with a singleton
            }
        });

        e.addBinding(new NodeSeniContext("set-colour", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 1, keyword());

                CoreBridge.setColour(mSeniContext.getPaint(),
                                     Node.asColourValue(params.get(0)));

                return new NodeNull();
            }
        });

        e.addBinding(new NodeSeniContext("rotate", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 1, keyword());

                float angle = Node.asFloatValue(params.get(0));

                mSeniContext.getCanvas().rotate(angle);

                return new NodeNull();
            }
        });

        e.addBinding(new NodeSeniContext("translate", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                Binder.checkArity(params, 2, keyword());

                float x = Node.asFloatValue(params.get(0));
                float y = Node.asFloatValue(params.get(1));

                mSeniContext.getCanvas().translate(x,y);

                return new NodeNull();
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

                return new NodeNull();
            }
        });

        return e;
    }
}
