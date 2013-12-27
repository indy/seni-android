package io.indy.seni.dummy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import io.indy.seni.AppConfig;
import io.indy.seni.core.Colour;
import io.indy.seni.runtime.CoreBridge;

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

    public static void Draw(Canvas canvas, int width, int height) {
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
    (do-times 3 i
              (scope (translate focal-x focal-y)
                     (squ angle (idx triads i) box-radius)
                     (setq angle (+ angle (/ 360 3)))))))

;;; scope is a special form ???
;;; idx is a function -> array[index % array.length]


 */