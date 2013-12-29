package io.indy.seni.dummy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.core.Colour;
import io.indy.seni.lang.Env;
import io.indy.seni.lang.Interpreter;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;
import io.indy.seni.runtime.CoreBridge;
import io.indy.seni.runtime.Runtime;
import io.indy.seni.runtime.SeniContext;


public class Art1352 {

    private static final String TAG = "Art1352";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private static void squ(Canvas canvas, float angle, Colour colour, float boxRadius) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        CoreBridge.setColour(paint, colour);

        ifd("--save");
        canvas.save();
        ifd("--rotate " + angle);
        canvas.rotate(angle);
        ifd("--translate 0.0f 200.0f");
        canvas.translate(0.0f, 200.0f);
        ifd("--drawRect");
        canvas.drawRect(-boxRadius, -boxRadius, boxRadius, boxRadius, paint);
        ifd("--restore");
        canvas.restore();

        float br2 = boxRadius;

        float ang = 20.0f;
        float toCenterFactor = 15.0f;
        float shrinkFactor = 0.9f;
        float angDelta = 8;

        for(int i=0;i<10;i++) {
            br2 *= shrinkFactor;
            ang += angDelta;
            ifd("----save");
            canvas.save();
            ifd("----rotate " + (angle - ang));
            canvas.rotate(angle - ang);
            ifd("----translate " + "0.0f " + (200.0f - (i * toCenterFactor)));
            canvas.translate(0.0f, 200.0f - (i * toCenterFactor));
            ifd("----rect");
            canvas.drawRect(-br2, -br2, br2, br2, paint);
            ifd("----restore");
            canvas.restore();

            ifd("----save");
            canvas.save();
            ifd("----rotate " + (angle + ang));
            canvas.rotate(angle + ang);
            ifd("----translate " + "0.0f " + (200.0f - (i * toCenterFactor)));
            canvas.translate(0.0f, 200.0f - (i * toCenterFactor));
            ifd("----rect");
            canvas.drawRect(-br2, -br2, br2, br2, paint);
            ifd("----restore");
            canvas.restore();
        }
    }

    public static void Draw2(Canvas canvas, int width, int height) {
        ifd("Draw");

        // use colour
        Colour primary = Colour.fromRGB(0.1f, 0.6f, 0.8f, 0.3f);
        Colour[] triads = primary.triad();

        int boxRadius = width / 12;
        int focalX = width / 2;
        int focalY = height / 2;

        Colour[] c = {primary, triads[0], triads[1]};

        float angle = 0.0f;
        for(int i=0;i<3;i++) {
            ifd("-save");
            canvas.save();
            ifd("-translate " + focalX + " " + focalY);
            canvas.translate(focalX, focalY);
            squ(canvas, angle, c[i], boxRadius);
            ifd("-restore");
            canvas.restore();
            angle += 360.0f / 3.0f;
        }
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

    /*
    public static void DrawSketch(Canvas canvas, int width, int height) {
        ifd("Draw");

        Runtime rt = new Runtime();

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