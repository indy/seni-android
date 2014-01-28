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

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ColourTest {

    @Test
    public void tempTest2() {

        Colour rgb = Colour.fromRGB(0.2f, 0.1f, 0.5f);
        Colour hsl = Colour.fromHSL(255.0f, 0.666f, 0.3f);
        Colour lab = Colour.fromLAB(19.9072f, 39.6375f, -52.7720f);

        assertThat(rgb.compare(rgb.as(Colour.Format.RGB))).isTrue();
        assertThat(rgb.compare(hsl.as(Colour.Format.RGB))).isTrue();
        assertThat(rgb.compare(lab.as(Colour.Format.RGB))).isTrue();

        assertThat(hsl.compare(rgb.as(Colour.Format.HSL))).isTrue();
        assertThat(hsl.compare(hsl.as(Colour.Format.HSL))).isTrue();
        assertThat(hsl.compare(lab.as(Colour.Format.HSL))).isTrue();

        assertThat(lab.compare(rgb.as(Colour.Format.LAB))).isTrue();
        assertThat(lab.compare(hsl.as(Colour.Format.LAB))).isTrue();
        assertThat(lab.compare(lab.as(Colour.Format.LAB))).isTrue();

    }
}
