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
import io.indy.seni.lang.NodeFloat;
import io.indy.seni.lang.NodeInt;
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

        String code = ""
                + "(define piece"
                + "  (lambda (primary secondary box-radius interp-fn)"
                + "    (let ((comp-primary (complementary primary))"
                + "          (comp-secondary (complementary secondary))"
                + "          (box-colour (colour 1.0 1.0 1.0 1.0)))"
                + "      (scope (set-colour box-colour)"
                + "             (rect (* -1.0 box-radius) (* -1.0 box-radius)"
                + "                   (*  1.0 box-radius) (*  1.0 box-radius))"
                + "             (set-colour primary)"
                + "             (rect (* -1.0  box-radius) (* -1.0  box-radius)"
                + "                   (*  1.0 box-radius) (* -7.0 box-radius))"
                + "             (set-colour comp-primary)"
                + "             (rect (*  -1.0  box-radius) (* 1.0  box-radius)"
                + "                   (*  1.0 box-radius) (* 7.0 box-radius))"
                + "             (set-colour secondary)"
                + "             (rect (*  -1.0 box-radius) (* -1.0 box-radius)"
                + "                   (*  -5.0 box-radius) (*  1.0 box-radius))"
                + "             (set-colour comp-secondary)"
                + "             (rect (*  1.0 box-radius) (* -1.0 box-radius)"
                + "                   (*  5.0 box-radius) (*  1.0 box-radius))"
                + "             (set-colour (interp-fn primary secondary 0.5))"
                + "             (rect (*  -1.0 box-radius) (* -1.0 box-radius)"
                + "                   (*  -5.0 box-radius) (* -7.0 box-radius))"
                + "             (set-colour (interp-fn comp-primary secondary 0.5))"
                + "             (rect (*  -1.0 box-radius) (*  1.0 box-radius)"
                + "                   (*  -5.0 box-radius) (*  7.0 box-radius))"
                + "             (set-colour (interp-fn primary comp-secondary 0.5))"
                + "             (rect (*  1.0 box-radius) (* -1.0 box-radius)"
                + "                   (*  5.0 box-radius) (* -7.0 box-radius))"
                + "             (set-colour (interp-fn comp-primary comp-secondary 0.5))"
                + "             (rect (*  1.0 box-radius) (*  1.0 box-radius)"
                + "                   (*  5.0 box-radius) (*  7.0 box-radius))))))"
                + ""
                + "(let ((primary (colour 0.1 0.6 0.8 1.0))"
                + "      (secondary (colour 1.0 0.0 0.8 1.0))"
                + "      (box-radius (/ canvas-width 12.0))"
                + "      (focal-x (/ canvas-width 2.0))"
                + "      (focal-y (/ canvas-height 2.0))"
                + "      (angle 38.0))"
                + "  (scope (translate (* canvas-width 0.25) (* canvas-height 0.25))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.1 0.6 0.8 1.0) (colour 1.0 0.0 0.8 1.0)"
                + "                box-radius interpolate-lab))"
                + "  (scope (translate (* canvas-width 0.75) (* canvas-height 0.25))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.5 0.1 0.8 1.0) (colour 1.0 0.0 0.8 1.0)"
                + "                box-radius interpolate-lab))"
                + ""
                + "  (scope (translate (* canvas-width 0.25) (* canvas-height 0.5))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.1 0.6 0.8 1.0) (colour 0.2 0.8 0.8 1.0)"
                + "                box-radius interpolate-lab))"
                + "  (scope (translate (* canvas-width 0.75) (* canvas-height 0.5))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.1 0.6 0.0 1.0) (colour 0.5 0.5 0.8 1.0)"
                + "                box-radius interpolate-lab))"
                + ""
                + "  (scope (translate (* canvas-width 0.25) (* canvas-height 0.75))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.1 0.6 0.1 1.0) (colour 0.8 0.2 0.4 1.0)"
                + "                box-radius interpolate-lab))"
                + "  (scope (translate (* canvas-width 0.75) (* canvas-height 0.75))"
                + "         (rotate angle)"
                + "         (scale 0.25 0.25)"
                + "         (piece (colour 0.6 0.2 0.3 1.0) (colour 0.7 0.2 0.8 1.0)"
                + "                box-radius interpolate-lab)))"
                + "";

        Runtime rt = new Runtime();

        Env env = new Env();
        env = rt.bindCoreFunctions(env);
        env = rt.bindPlatformFunctions(env, sc);

        env = env.addBinding("canvas-width", new NodeFloat((float)width));
        env = env.addBinding("canvas-height", new NodeFloat((float)height));

        List<Node> ast = rt.asAst(code);

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


