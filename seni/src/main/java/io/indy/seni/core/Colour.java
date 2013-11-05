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

public abstract class Colour {

    public enum Format {
        RGB,
        HSL,
        LAB,
        HSV,
        XYZ                     // private internal format
    }

    // static Colour creation methods
    // 
    public static Colour fromRGB(float r, float g, float b) {
        return new ColourRGB(r, g, b, 1.0f);
    }

    public static Colour fromRGB(float r, float g, float b, float alpha) {
        return new ColourRGB(r, g, b, alpha);
    }

    public static Colour fromHSL(float h, float s, float l) {
        return new ColourHSL(h, s, l, 1.0f);
    }

    public static Colour fromHSL(float h, float s, float l, float alpha) {
        return new ColourHSL(h, s, l, alpha);
    }

    public static Colour fromHSV(float h, float s, float v) {
        return new ColourHSV(h, s, v, 1.0f);
    }

    public static Colour fromHSV(float h, float s, float v, float alpha) {
        return new ColourHSV(h, s, v, alpha);
    }

    public static Colour fromLAB(float l, float a, float b) {
        return new ColourLAB(l, a, b, 1.0f);
    }

    public static Colour fromLAB(float l, float a, float b, float alpha) {
        return new ColourLAB(l, a, b, alpha);
    }

    private static Colour fromXYZ(float x, float y, float z) {
        return new ColourXYZ(x, y, z, 1.0f);
    }

    private static Colour fromXYZ(float x, float y, float z, float alpha) {
        return new ColourXYZ(x, y, z, alpha);
    }

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private static final int ALPHA = 3;

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    private static final int L = 0;
    private static final int A = 1;
    private static final int B = 2;

    private static final int H = 0;
    private static final int S = 1;
    private static final int V = 2;

    protected Format mFormat;
    protected float[] mVal;

    private Colour(Format fmt, float v1, float v2, float v3, float v4) {
        mFormat = fmt;
        mVal = new float[] {v1, v2, v3, v4};
    }

    public abstract Colour as(Format f);

    public String toString() {
        return mFormat + ": " + mVal[0] + ", " + mVal[1] + ", "+ mVal[2] + ", " + mVal[3];
    };

    public String scribe() {
        if(mVal[ALPHA] == 1.0f) {
            return "" + mVal[0] + " " + mVal[1] + " " + mVal[2];
        }
        return "" + mVal[0] + " " + mVal[1] + " " + mVal[2] + " " + mVal[3];
    }

    public boolean compare(Colour c) {
        if (c.mFormat != this.mFormat) {
            return false;
        }
        
        float tolerance = 0.05f;

        for (int i=0;i<4;i++) {
            if (Math.abs(c.mVal[i] - this.mVal[i]) > tolerance) {
                return false;
            }
        }

        return true;
    }


    private static final float sUnitAngle = 360.0f / 12.0f;
    private static final float sComplimentaryAngle = sUnitAngle * 6;
    private static final float sTriadAngle = sUnitAngle * 4;

    private Colour addAngleToHSL(float delta) {

        // c is be a copy of this, but in HSL format
        ColourHSL c = (ColourHSL)(this.as(Format.HSL));

        // rotate the hue by the given delta
        c.mVal[H] = (c.mVal[H] + delta) % 360.0f;

        return c;
    }

    // Return the 2 colours either side of this that are 'ang' degrees away
    private Colour[] pair(float ang) {
        Colour[] ret = {addAngleToHSL(-ang), addAngleToHSL(ang)};
        return ret;
    }

    // Returns the colour at the opposite end of the wheel
    //
    public Colour complementary() {
        return addAngleToHSL(sComplimentaryAngle);
    }

    // Returns the 2 colours next to a complementary colour. 
    // e.g. if the input colour is at the 12 o'clock position, 
    // this will return the 5 o'clock and 7 o'clock colours
    //
    public Colour[] splitComplementary() {
        return complementary().pair(sUnitAngle);
    }

    // Returns the adjacent colours. 
    // e.g. given a colour at 3 o'clock this will return the
    // colours at 2 o'clock and 4 o'clock
    //
    public Colour[] analagous() {
        return pair(sUnitAngle);
    }

    // Returns the 2 colours that will result in all 3 colours 
    // being evenly spaced around the colour wheel. 
    // e.g. given 12 o'clock this will return 4 o'clock and 8 o'clock
    //
    public Colour[] triad() {
        return pair(sTriadAngle);
    }

    /*
      http://www.brucelindbloom.com/index.html?Equations.html

      l 0 -> 100  lightness
      a -128 -> +127   green -> red
      b -128 -> +127   cyan -> yellow
    */

    protected float colourToAxis(float c) {
        float temp;
        if (c > 0.04045f) {
            temp = (float) Math.pow((c + 0.055f) / 1.055f , 2.4f);
        } else {
            temp = c / 12.92f;
        }
        return temp * 100.0f;
    }

    protected Colour RGBToXYZ() {
        // assumes that this is already in RGB format
        float rr = colourToAxis(mVal[RED]);
        float gg = colourToAxis(mVal[GREEN]);
        float bb = colourToAxis(mVal[BLUE]);

        return Colour.fromXYZ((rr * 0.4124f) + (gg * 0.3576f) + (bb * 0.1805f),
                              (rr * 0.2126f) + (gg * 0.7152f) + (bb * 0.0722f),
                              (rr * 0.0193f) + (gg * 0.1192f) + (bb * 0.9505f),
                              mVal[ALPHA]);
    }

    protected float axisToLABComponent(float a) {
        if (a > 0.008856f) {
            return (float) Math.pow(a, 1.0f / 3.0f);
        } else {
            return (7.787f * a) + (16.0f / 116.0f);
        }
    }

    protected Colour XYZToLAB() {
        // assumes that this is already in XYZ format
        float xx = axisToLABComponent(mVal[X] / 95.047f);
        float yy = axisToLABComponent(mVal[Y] / 100.000f);
        float zz = axisToLABComponent(mVal[Z] / 108.883f);

        return Colour.fromLAB((116.0f * yy) - 16.0f,
                              500.0f * (xx - yy),
                              200.0f * (yy - zz),
                              mVal[ALPHA]);
    }

    protected float AxisToColour(float a) {
        if (a > 0.0031308f) {
            return (1.055f * (float)Math.pow(a, 1.0f / 2.4f)) - 0.055f;
        } else {
            return a * 12.92f;
        }
    }

    protected Colour XYZToRGB() {
        float xx = mVal[X] / 100.0f;
        float yy = mVal[Y] / 100.0f;
        float zz = mVal[Z] / 100.0f;

        float r = (xx * 3.2406f) + (yy * -1.5372f) + (zz * -0.4986f);
        float g = (xx * -0.9689f) + (yy * 1.8758f) + (zz * 0.0415f);
        float b = (xx * 0.0557f) + (yy * -0.2040f) + (zz * 1.0570f);

        return Colour.fromRGB(AxisToColour(r),
                              AxisToColour(g),
                              AxisToColour(b));
    }

    protected int maxChannel() {
        int hi = mVal[RED] > mVal[GREEN] ? RED : GREEN;
        return mVal[BLUE] > mVal[hi] ? BLUE : hi;
    }

    protected int minChannel() {
        int hi = mVal[RED] < mVal[GREEN] ? RED : GREEN;
        return mVal[BLUE] < mVal[hi] ? BLUE : hi;
    }

    protected float hue(int maxChan, float chroma) {
        if (chroma == 0.0f) {
            return 0.0f;        // invalid hue
        } 
        switch(maxChan) {
        case RED: 
            return 60.0f * (float)(((mVal[GREEN] - mVal[BLUE]) / chroma) % 6);
        case GREEN:
            return 60.0f * (((mVal[BLUE] - mVal[RED]) / chroma) + 2.0f);
        case BLUE: 
            return 60.0f * (((mVal[RED] - mVal[GREEN]) / chroma) + 4.0f);
        };
        return 0.0f;            // should never get here
    }

    protected Colour RGBToHSL() {
        int minCh = minChannel();
        float minVal = mVal[minCh];

        int maxCh = maxChannel();
        float maxVal = mVal[maxCh];

        float chroma = maxVal - minVal;
        float h = hue(maxCh, chroma);
        boolean validHue = (chroma != 0.0f);

        float lightness = 0.5f * (minVal + maxVal);
        float saturation;
        if(chroma == 0.0f) {
            saturation = 0.0f;
        } else {
            saturation = chroma / (1.0f - Math.abs((2.0f * lightness) - 1.0f));
        }

        ColourHSL ret = new ColourHSL(h, saturation, lightness, 1.0f);
        ret.setValidHue(validHue);
        return ret;
    }

    protected Colour RGBToHSV() {


        int minCh = minChannel();
        float minVal = mVal[minCh];

        int maxCh = maxChannel();
        float maxVal = mVal[maxCh];

        float chroma = maxVal - minVal;
        float h = hue(maxCh, chroma);
        boolean validHue = (chroma != 0.0f);

        float value = maxVal;

        float saturation;
        if(chroma == 0.0f) {
            saturation = 0.0f;
        } else {
            saturation = chroma / value;
        }

        ColourHSV ret = new ColourHSV(h, saturation, value, 1.0f);
        ret.setValidHue(validHue);
        return ret;
    }

    protected Colour CHMToRGB(float chroma, float h, float m, boolean validHue) {
        if (!validHue) {
            return Colour.fromRGB(m, m, m);
        }

        float hprime = h / 60.0f;
        float x = chroma * (1.0f - Math.abs((hprime % 2) - 1.0f));
        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;

        if (hprime  < 1.0f) {
            r = chroma; g = x; b = 0.0f;
        } else if (hprime < 2.0f) {
            r = x; g = chroma; b = 0.0f;
        } else if (hprime < 3.0f) {
            r = 0.0f; g = chroma; b = x;
        } else if (hprime < 4.0f) {
            r = 0.0f; g = x; b = chroma;
        } else if (hprime < 5.0f) {
            r = x; g = 0.0f; b = chroma;
        } else if (hprime < 6.0f) {
            r = chroma; g = 0.0f; b = x;
        } 
        
        return Colour.fromRGB(r + m, g + m, b + m);
    }

    protected Colour HSLToRGB(boolean validHue) {
        float h = mVal[H];
        float s = mVal[S];
        float l = mVal[2]; // L already defined for LAB ...bugger
        float chroma = (1.0f - Math.abs((2.0f * l) - 1.0f)) * s;
        float m = l - (0.5f * chroma);
        return CHMToRGB(chroma, h, m, validHue);
    }


    protected float LABComponentToAxis(float l) {
        if((float)Math.pow(l, 3.0f) > 0.008856) {
            return (float)Math.pow(l, 3.0f);
        } else {
            return (l - (16.0f / 116.0f)) / 7.787f;
        }
    }

    protected Colour LABToXYZ() {
        float refX = 95.047f;
        float refY = 100.000f;
        float refZ = 108.883f;

        float y = (mVal[L] + 16.0f) / 116.0f;
        float x = (mVal[A] / 500.0f) + y;
        float z = y - (mVal[B] / 200.0f);

        float xx = LABComponentToAxis(x);
        float yy = LABComponentToAxis(y);
        float zz = LABComponentToAxis(z);
        
        return Colour.fromXYZ(refX * xx, 
                              refY * yy, 
                              refZ * zz);
    }

    protected Colour HSVToRGB(boolean validHue) {
        float h = mVal[H];
        float s = mVal[S];
        float v = mVal[V];
        float chroma = v * s;
        float m = v - chroma;
        return CHMToRGB(chroma, h, m, validHue);
    }


    private static class ColourLAB extends Colour {

        public ColourLAB(float l, float a, float b, float alpha) {
            super(Colour.Format.LAB, l, a, b, alpha);
        }

        public Colour as(Format f) {

            if(f == Format.RGB) {
                return LABToXYZ().XYZToRGB();
            } else if(f == Format.HSV) {
                return LABToXYZ().XYZToRGB().RGBToHSV();
            } else if(f == Format.HSL) {
                return LABToXYZ().XYZToRGB().RGBToHSL();
            } else if(f == Format.LAB) {
                return Colour.fromLAB(mVal[0], mVal[1], mVal[2], mVal[3]);
            }

            // TODO: throw an error
            return this;
        }
    }

    private static class ColourHSV extends Colour {

        private boolean mValidHue;

        public ColourHSV(float h, float s, float v, float alpha) {
            super(Colour.Format.HSV, h, s, v, alpha);
            mValidHue = true;
        }

        public void setValidHue(boolean isValid) {
            mValidHue = isValid;
        }

        public Colour as(Format f) {

            if(f == Format.RGB) {
                return HSVToRGB(mValidHue);
            } else if(f == Format.HSV) {
                return Colour.fromHSV(mVal[0], mVal[1], mVal[2], mVal[3]);
            } else if(f == Format.HSL) {
                return HSVToRGB(mValidHue).RGBToHSL();
            } else if(f == Format.LAB) {
                return HSVToRGB(mValidHue).RGBToXYZ().XYZToLAB();
            }

            // TODO: throw an error
            return this;
        }
    }

    private static class ColourHSL extends Colour {
        
        private boolean mValidHue;

        public ColourHSL(float h, float s, float l, float alpha) {
            super(Colour.Format.HSL, h, s, l, alpha);
            mValidHue = true;
        }

        public void setValidHue(boolean isValid) {
            mValidHue = isValid;
        }

        public Colour as(Format f) {

            if(f == Format.RGB) {
                return HSLToRGB(mValidHue);
            } else if(f == Format.HSV) {
                return HSLToRGB(mValidHue).RGBToHSV();
            } else if(f == Format.HSL) {
                return Colour.fromHSL(mVal[0], mVal[1], mVal[2], mVal[3]);
            } else if(f == Format.LAB) {
                return HSLToRGB(mValidHue).RGBToXYZ().XYZToLAB();
            }

            // TODO: throw an error
            return this;
        }
    }

    private static class ColourRGB extends Colour {

        public ColourRGB(float r, float g, float b, float alpha) {
            super(Colour.Format.RGB, r, g, b, alpha);
        }

        public Colour as(Format f) {

            if(f == Format.RGB) {
                return Colour.fromRGB(mVal[0], mVal[1], mVal[2], mVal[3]);
            } else if(f == Format.HSV) {
                return RGBToHSV();
            } else if(f == Format.HSL) {
                return RGBToHSL();
            } else if(f == Format.LAB) {
                return RGBToXYZ().XYZToLAB();
            }

            // TODO: throw an error
            return this;
        }
    }

    // an internal colour representation that's used 
    // during various conversion processes
    // 
    private static class ColourXYZ extends Colour {
        public ColourXYZ(float x, float y, float z, float alpha) {
            super(Colour.Format.XYZ, x, y, z, alpha);
        }

        public Colour as(Format f) {
            // TODO: throw an error
            return this;
        }
    }

}
