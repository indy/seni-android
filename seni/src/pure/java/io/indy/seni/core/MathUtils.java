
package io.indy.seni.core;

public class MathUtils {

    public static float interpolate(float a, float b, float t) {
        return (a * (1.0f - t)) + (b * t);
    }
}



