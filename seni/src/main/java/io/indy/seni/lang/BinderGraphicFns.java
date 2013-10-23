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

import java.util.List;
import java.util.Iterator;

public class BinderGraphicFns extends Binder {

    public static Env bind(Env e) {

        // create a colour node
        e.addBinding(new NodeFn("colour") {
                public Node execute(Env env, List<Node> params) 
                    throws LangException {

                    // currently just rgb
                    // TODO: accept rgba, another colour object, hsl etc
                    Binder.checkArity(params, 3, keyword());

                    float r = Node.asFloatValue(params.get(0));
                    float g = Node.asFloatValue(params.get(1));
                    float b = Node.asFloatValue(params.get(2));

                    return new NodeColour(r, g, b);
                }
            });

        return e;
    }
}
