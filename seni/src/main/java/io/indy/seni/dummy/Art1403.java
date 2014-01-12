package io.indy.seni.dummy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.runtime.Runtime;
import io.indy.seni.runtime.SeniContext;


public class Art1403 {

    private static final String TAG = "Art1403";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static void Draw(Canvas canvas, int width, int height) {
        ifd("Draw");

        SeniContext sc = new SeniContext(canvas);
        Paint paint = sc.getPaint();
        paint.setARGB(250, 250, 0, 0);

        ifd("width " + width);
        ifd("height " + height);

        String code3 = ""
                + "(define squ "
                + "  (lambda (angle colour box-radius)"
                + "    (begin (set-colour colour)"
                + "        (scope (rotate angle)"
                + "               (translate 0.0 200.0)"
                + "               (rect (* -1.0 box-radius) (* -1.0 box-radius) box-radius box-radius))"
                + "      (let ((br2 box-radius)"
                + "            (ang 20.0)"
                + "            (to-center-factor 15.0)"
                + "            (shrink-factor 0.9)"
                + "            (ang-delta 8.0))"
                + "        (do-times i 10"
                + "                  (set! br2 (* br2 shrink-factor))"
                + "                  (set! ang (+ ang ang-delta))"
                + "                  (scope (rotate (- angle ang))"
                + "                         (translate 0.0 (- 200.0 (* (as-float i) to-center-factor)))"
                + "                         (rect (* -1.0 br2) (* -1.0 br2) br2 br2))"
                + "                  (scope (rotate (+ angle ang))"
                + "                         (translate 0.0 (- 200.0 (* (as-float i) to-center-factor)))"
                + "                         (rect (* -1.0 br2) (* -1.0 br2) br2 br2)))))))"
                + ""
                + "(let ((canvas-width 768.0)"
                + "      (canvas-height 1038.0)"
                + "      (primary (colour 0.1 0.6 0.8 0.3))"
                + "      (triads (triad primary))"
                + "      (box-radius (/ canvas-width 12.0))"
                + "      (focal-x (/ canvas-width 2.0))"
                + "      (focal-y (/ canvas-height 2.0))"
                + "      (angle 0.0))"
                + "  (do-times i 3"
                + "            (scope (translate focal-x focal-y)"
                + "                   (squ angle (nth triads i) box-radius)"
                + "                   (set! angle (+ angle (/ 360.0 3.0))))))";

        Runtime rt = new Runtime();

        Env env = new Env();
        env = rt.bindCoreFunctions(env);
        env = rt.bindPlatformFunctions(env, sc);

        List<Node> ast = rt.asAst(code3);

        try {
            for (Node node : ast) {
                Interpreter.eval(env, node);
            }
        } catch(LangException e) {
            ifd("exception: " + e);
            e.printStackTrace();
        }

        //canvas.drawRect(50.0f, 50.0f, 300.0f, 300.f, sc.getPaint());
    }
}
