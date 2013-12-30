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

        canvas.save();
        canvas.rotate(angle);
        canvas.translate(0.0f, 200.0f);
        canvas.drawRect(-boxRadius, -boxRadius, boxRadius, boxRadius, paint);
        canvas.restore();

        float br2 = boxRadius;

        float ang = 20.0f;
        float toCenterFactor = 15.0f;
        float shrinkFactor = 0.9f;
        float angDelta = 8;

        for(int i=0;i<10;i++) {
            br2 *= shrinkFactor;
            ang += angDelta;

            canvas.save();
            canvas.rotate(angle - ang);
            canvas.translate(0.0f, 200.0f - (i * toCenterFactor));
            canvas.drawRect(-br2, -br2, br2, br2, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(angle + ang);
            canvas.translate(0.0f, 200.0f - (i * toCenterFactor));
            canvas.drawRect(-br2, -br2, br2, br2, paint);
            canvas.restore();
        }
    }

    public static void DrawWorking(Canvas canvas, int width, int height) {
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
            canvas.save();
            canvas.translate(focalX, focalY);
            squ(canvas, angle, c[i], boxRadius);
            canvas.restore();
            angle += 360.0f / 3.0f;
        }
    }


    public static void Draw(Canvas canvas, int width, int height) {
        ifd("Draw");

        SeniContext sc = new SeniContext(canvas);
        Paint paint = sc.getPaint();
        paint.setARGB(250, 250, 0, 0);

        String code1 = "(rect 50.0 50.0 290.0 200.0)";

        String code = ""
                + "(scope"
                + "  (set-colour (colour 0.0 1.0 0.0))"
                + "  (rotate -10.0)"
                + "  (rect 150.0 150.0 290.0 250.0)"
                + ")";

        Runtime rt = new Runtime();

        Env env = new Env();
        env = rt.bindCoreFunctions(env);
        env = rt.bindPlatformFunctions(env, sc);

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
 proposed equivalent seni syntax:



(defn squ (angle colour box-radius)
  (set-colour colour)
  (scope (rotate angle)
         (translate 0 200)
         (rect -box-radius -box-radius box-radius box-radius))
  (let ((br2 box-radius)
        (ang [20])
        (to-center-factor [15])
        (shrink-factor [0.9])
        (ang-delta [8]))
    (do-times [10] i
              (setq br2 (* br2 shrink-factor))
              (setq ang (* ang ang-delta))
              (scope (rotate (- angle ang))
                     (translate (0.0 (- 200.0 (* i to-center-factor))))
                     (rect -br2 -br2 br2 br2))
              (scope (rotate (+ angle ang))
                     (translate (0.0 (- 200.0 (* i to-center-factor))))
                     (rect -br2 -br2 br2 br2)))))

(defdraw art1352 ()
  (let ((primary (fromRGB 0.1 0.6 0.8 0.3))
        (triads ([as-triad] primary)) ; returns 3 elements incl. primary
        (box-radius (/ canvas-width 12))
        (focal-x (/ canvas-width 2))
        (focal-y (/ canvas-height 2))
        (angle 0))
    (do-times i 3
              (scope (translate focal-x focal-y)
                     (squ angle (idx triads i) box-radius)
                     (setq angle (+ angle (/ 360 3)))))))

;;; scope is a special form ???
;;; idx is a function -> array[index % array.length]


 */