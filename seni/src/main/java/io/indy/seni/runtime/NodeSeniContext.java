package io.indy.seni.runtime;

import android.util.Log;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.NodeFn;

abstract public class NodeSeniContext extends NodeFn {

    private static final String TAG = "NodeSeniContext";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    protected SeniContext mSeniContext;

    public NodeSeniContext(String keyword, SeniContext seniContext) {
        super(keyword);

        mSeniContext = seniContext;
    }
}
