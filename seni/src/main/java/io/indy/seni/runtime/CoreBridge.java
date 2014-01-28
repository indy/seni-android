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

import android.graphics.Paint;
import android.util.Log;

import io.indy.seni.AppConfig;
import io.indy.seni.core.Colour;

public class CoreBridge {

    private static final String TAG = "CoreBridge";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static Paint setColour(Paint paint, Colour colour) {
        float[] f = colour.as(Colour.Format.RGB).getVals();

        paint.setARGB((int) (f[Colour.ALPHA] * 255),
                (int) (f[Colour.RED] * 255),
                (int) (f[Colour.GREEN] * 255),
                (int) (f[Colour.BLUE] * 255));
        return paint;
    }

}
