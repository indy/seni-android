package io.indy.seni.lang;

import java.util.HashMap;

public class Env {

    private Env mOuter;
    private HashMap<String, Node> mBindings;

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

    public Node lookup(String key) {

        Env e = this;
        do {
            if(e.mBindings.containsKey(key)) {
                return e.mBindings.get(key);
            }
            e = e.mOuter;
        } while(e != null);
        
        throw new NullPointerException("unable to find value of key: " + key + " in env");
    }
}
