/*
 * Copyright 2013 Inderjit Gill
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

package io.indy.seni.core;

public class Perlin {

    public double noise(double x, double y, double z) {
        return ImprovedNoise.noise(x, y, z);
    }

    public double noiseRepeatingX(double x, double y, double z, double period) {
        return (((period - x) * noise(x, y, z)) +
                (x * noise((x - period), y, z))) / period;
    }

    public double noiseRepeatingY(double x, double y, double z, double period) {
        return (((period - y) * noise(x, y, z)) +
                (y * noise(x, (y - period), z))) / period;
    }

    public double noiseRepeatingZ(double x, double y, double z, double period) {
        return (((period - z) * noise(x, y, z)) +
                (z * noise(x, y, (z - period)))) / period;
    }
}



