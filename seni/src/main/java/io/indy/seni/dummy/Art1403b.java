package io.indy.seni.dummy;

import android.util.Log;

import io.indy.seni.AppConfig;

public class Art1403b {

    private static final String TAG = "Art1403b";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static String script() {

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
                + "(let ((primary (colour [0.1] [0.6] [0.8] 1.0))"
                + "      (secondary (colour [1.0] [0.0] [0.8] 1.0))"
                + "      (box-radius (/ canvas-width 12.0))"
                + "      (focal-x (/ canvas-width 2.0))"
                + "      (focal-y (/ canvas-height 2.0))"
                + "      (angle 38.0))"
                + "  (scope (translate focal-x focal-y)"
                + "         (rotate angle)"
                + "         (piece primary secondary box-radius interpolate-lab)))"
                + "";

        return code;
    }

}
