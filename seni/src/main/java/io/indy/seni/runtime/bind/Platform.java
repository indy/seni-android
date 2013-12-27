package io.indy.seni.runtime.bind;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Binder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeBoolean;
import io.indy.seni.lang.NodeFn;
import io.indy.seni.lang.NodeLambda;
import io.indy.seni.lang.NodeList;
import io.indy.seni.lang.NodeNull;
import io.indy.seni.runtime.NodeSeniContext;
import io.indy.seni.runtime.SeniContext;

public class Platform extends Binder {

    private static final String TAG = "Platform";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static Env bind(Env e, SeniContext sc) {

        e.addBinding(new NodeSeniContext("scope", sc) {
            public Node execute(Env env, List<Node> params)
                    throws LangException {

                mSeniContext.save();

                // execute all the expressions

                mSeniContext.restore();

                return new NodeNull(); // todo: replace with a singleton
            }
        });

        return e;
    }
}
