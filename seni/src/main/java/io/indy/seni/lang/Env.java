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

import io.indy.seni.core.bind.Core;
import io.indy.seni.lang.bind.Functional;
import io.indy.seni.lang.bind.Lists;
import io.indy.seni.lang.bind.Mathematical;

public class Env {

    private Env mOuter;
    private HashMap<String, Node> mBindings;

    public static Env bindCoreFuns(Env env) {
        // binds core functions in a new scope of env
        Env e = env.newScope();

        e = Mathematical.bind(e);
        e = Functional.bind(e);
        e = Lists.bind(e);
        e = Core.bind(e);

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
}
