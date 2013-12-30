package io.indy.seni.runtime;

import android.util.Log;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.NodeSpecial;

abstract public class NodeSpecialSeniContext extends NodeSpecial {

    private static final String TAG = "NodeSpecialSeniContext";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    protected SeniContext mSeniContext;

    public NodeSpecialSeniContext(String keyword, SeniContext seniContext) {
        super(keyword);

        mSeniContext = seniContext;
    }
}
