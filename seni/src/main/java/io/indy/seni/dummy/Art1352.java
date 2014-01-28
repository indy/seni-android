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

package io.indy.seni.dummy;

import android.util.Log;

import io.indy.seni.AppConfig;


public class Art1352 {

    private static final String TAG = "Art1352";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static String script() {
        String code3 = ""
                + "(define squ "
                + "  (lambda (angle colour box-radius)"
                + "    (begin (set-colour colour)"
                + "        (scope (rotate angle)"
                + "               (translate 0.0 (/ canvas-height 5))"
                + "               (rect (* -1.0 box-radius) (* -1.0 box-radius) box-radius box-radius))"
                + "      (let ((br2 box-radius)"
                + "            (ang 20.0)"
                + "            (to-center-factor (/ (/ canvas-height 5) 13.3333))"
                + "            (shrink-factor 0.9)"
                + "            (ang-delta 8.0))"
                + "        (do-times i 10"
                + "                  (set! br2 (* br2 shrink-factor))"
                + "                  (set! ang (+ ang ang-delta))"
                + "                  (scope (rotate (- angle ang))"
                + "                         (translate 0.0 (- (/ canvas-height 5) (* (as-float i) to-center-factor)))"
                + "                         (rect (* -1.0 br2) (* -1.0 br2) br2 br2))"
                + "                  (scope (rotate (+ angle ang))"
                + "                         (translate 0.0 (- (/ canvas-height 5) (* (as-float i) to-center-factor)))"
                + "                         (rect (* -1.0 br2) (* -1.0 br2) br2 br2)))))))"
                + ""
                + "(let ((primary (colour 0.1 0.6 0.8 0.3))"
                + "      (triads (triad primary))"
                + "      (box-radius (/ canvas-width 12.0))"
                + "      (focal-x (/ canvas-width 2.0))"
                + "      (focal-y (/ canvas-height 2.0))"
                + "      (angle 0.0))"
                + "  (do-times i 3"
                + "            (scope (translate focal-x focal-y)"
                + "                   (squ angle (nth triads i) box-radius)"
                + "                   (set! angle (+ angle (/ 360.0 3.0))))))";

        return code3;
    }


    /*
    public static void DrawSketch(Canvas canvas, int width, int height) {
        ifd("Draw");

        SeniRuntime rt = new SeniRuntime();

        String code = "(+ 1 1)";
        List<Node> ast = rt.asAst(code);

        Env env = new Env();
        env = rt.bindCoreFunctions(env);

        SeniContext sc = null;

        try {
            Node setupNode = findSetupNode(ast);
            Interpreter.eval(env, setupNode);
            // check env for particular vars, use their values to configure the canvas
            sc = buildSeniContextFromEnv(env);

        } catch(LangException e) {
            ifd("exception: " + e);
            e.printStackTrace();
        }

        env = rt.bindPlatformFunctions(env, sc);

        try {
            for (Node node : ast) {
                Interpreter.eval(env, node);
            }
        } catch(LangException e) {
            ifd("exception: " + e);
            e.printStackTrace();
        }
    }
    */
}



/*
(setup
 (aspect-ratio square)
 (primary-colour (colour 0.1 0.6 0.8 0.3))
 (colour-scheme triad))

(defn squ (angle colour box-radius)
  (set-colour colour)
  (scope (rotate angle)
         (translate 0.0 200.0)
         (rect (* -1.0 box-radius) (* -1.0 box-radius) box-radius box-radius))
  (let ((br2 box-radius)
        (ang 20.0)
        (to-center-factor 15.0)
        (shrink-factor 0.9)
        (ang-delta 8.0))
    (do-times i 10
              (set! br2 (* br2 shrink-factor))
              (set! ang (+ ang ang-delta))
              (scope (rotate (- angle ang))
                     (translate 0.0 (- 200.0 (* i to-center-factor)))
                     (rect (neg br2) (neg br2) br2 br2))
              (scope (rotate (+ angle ang))
                     (translate 0.0 (- 200.0 (* i to-center-factor)))
                     (rect (neg br2) (neg br2) br2 br2)))))

(let ((box-radius (/ canvas-width 12.0))
      (focal-x (/ canvas-width 2.0))
      (focal-y (/ canvas-height 2.0))
      (angle 0.0))
  (do-times i 3
            (scope (translate focal-x focal-y)
                   (squ angle (looping-nth colour-scheme i) box-radius)
                   (set! angle (+ angle (/ 360.0 3.0))))))
 */