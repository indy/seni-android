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

public class Art1405 {

    private static final String TAG = "Art1405";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static String script() {

        String code = ""
                + "(let ((primary (colour [0.1] [0.6] [0.8] 1.0))"
                + "      (box-radius (/ canvas-width [3.0 (in-range 2.0 8.0)]))"
                + "      (focal-x (/ canvas-width 2.0))"
                + "      (focal-y (/ canvas-height 2.0))"
                + "      (angle [30.0 (in-range 15.0 60.0)]))"
                + "  (scope (translate focal-x focal-y)"
                + "         (rotate angle)"
                + "         (set-colour primary)"
                + "         (rect (* -1.0 box-radius) (* -1.0 box-radius)"
                + "               (*  1.0 box-radius) (*  1.0 box-radius))))"
                + "";

        return code;
    }

}
