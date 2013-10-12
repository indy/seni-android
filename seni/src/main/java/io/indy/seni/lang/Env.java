package io.indy.seni.lang;

import java.util.HashMap;

public class Env {

    private Env mOuter;
    private HashMap<String, Node> mBindings;

    public static Env bindCoreFuns(Env env) {
        // binds core functions in a new scope of env
        Env e = env.newScope();

        e.addBinding("+", new NodeLambdaPlus());
        e.addBinding("-", new NodeLambdaMinus());

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

    public boolean hasBinding(String key) {
        return mBindings.containsKey(key);
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
