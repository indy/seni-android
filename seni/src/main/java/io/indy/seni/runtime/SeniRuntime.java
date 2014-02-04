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

package io.indy.seni.runtime;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Genotype;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeFloat;
import io.indy.seni.runtime.bind.Platform;

public class SeniRuntime {

    private static final String TAG = "SeniRuntime";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static void render(Canvas canvas, String script) {
        AstHolder astHolder = new AstHolder(script);
        render(canvas, astHolder, astHolder.getGenotype());
    }

    public static void render(Canvas canvas, Genotype genotype) {
        render(canvas, genotype.getAstHolder(), genotype);
    }

    public static void render(Canvas canvas, AstHolder astHolder, Genotype genotype) {
        SeniContext sc = new SeniContext(canvas);
        Paint paint = sc.getPaint();
        paint.setARGB(250, 250, 0, 0);

        //ifd("width " + width);
        //ifd("canvas width: " + canvas.getWidth());
        //ifd("height " + height);
        //ifd("canvas height: " + canvas.getHeight());

        SeniRuntime rt = new SeniRuntime();

        Env env = new Env();
        // bind essential functions/values to the environment
        //
        env = rt.bindCoreFunctions(env);
        env = rt.bindPlatformFunctions(env, sc);
        env = env.addBinding("canvas-width", new NodeFloat((float) canvas.getWidth()));
        env = env.addBinding("canvas-height", new NodeFloat((float) canvas.getHeight()));

        // generate ast from script
        //
        List<Node> ast = astHolder.getAst();

        // bind alterable node values
        //
        env = genotype.bind(env);

        try {
            for (Node node : ast) {
                Interpreter.eval(env, node);
            }
        } catch (LangException e) {
            ifd("exception: " + e);
            e.printStackTrace();
        }
    }

    public SeniRuntime() {

    }

    public Env bindCoreFunctions(Env env) {
        return Env.bindCoreFuns(env);
    }

    /**
     * Bind the functions which require a valid SeniContext
     *
     * @return the Env with bound with platform specific vars
     */
    public Env bindPlatformFunctions(Env env, SeniContext sc) {
        return Platform.bind(env, sc);
    }


}
