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

package io.indy.seni.core.bind;

import org.junit.Test;

import io.indy.seni.core.Colour;
import io.indy.seni.lang.EvalTestBase;
import io.indy.seni.lang.LangException;
import io.indy.seni.lang.Node;

import static org.fest.assertions.api.Assertions.assertThat;


public class CoreTest extends EvalTestBase {

    @Test
    public void testColourCreation() {
        assertColour("(colour 0.2 0.3 0.5)",
                Colour.fromRGB(0.2f, 0.3f, 0.5f));
    }

    @Test
    public void testComplementary() {
        assertColour("(complementary (colour 0.2 0.3 0.5))",
                Colour.fromHSL(40.0f, 0.42857146f, 0.35f));
    }

    @Test
    public void testSplitComplementary() {
        assertColour("(nth (split-complementary (colour 0.2 0.3 0.5)) 0)",
                Colour.fromRGB(0.2f, 0.3f, 0.5f));

        assertColour("(nth (split-complementary (colour 0.2 0.3 0.5)) 1)",
                Colour.fromHSL(10.0f, 0.42857146f, 0.35f));

        assertColour("(nth (split-complementary (colour 0.2 0.3 0.5)) 2)",
                Colour.fromHSL(70.0f, 0.42857146f, 0.35f));
    }

    @Test
    public void testAnalagous() {
        assertColour("(nth (analagous (colour 0.2 0.3 0.5)) 0)",
                Colour.fromRGB(0.2f, 0.3f, 0.5f));

        assertColour("(nth (analagous (colour 0.2 0.3 0.5)) 1)",
                Colour.fromHSL(190.0f, 0.42857146f, 0.35f));

        assertColour("(nth (analagous (colour 0.2 0.3 0.5)) 2)",
                Colour.fromHSL(250.0f, 0.42857146f, 0.35f));
    }

    @Test
    public void testTriad() {
        assertColour("(nth (triad (colour 0.2 0.3 0.5)) 0)",
                Colour.fromRGB(0.2f, 0.3f, 0.5f));

        assertColour("(nth (triad (colour 0.2 0.3 0.5)) 1)",
                Colour.fromHSL(100.0f, 0.42857146f, 0.35f));

        assertColour("(nth (triad (colour 0.2 0.3 0.5)) 2)",
                Colour.fromHSL(340.0f, 0.42857146f, 0.35f));
    }

    protected void assertColour(String code, Colour expected) {

        try {
            Node n = run(code);
            Colour actual = Node.asColourValue(n);

            String errorMsg = "eval: " + code + " => " + actual + " [expected: " + expected + "]";
            assertThat(actual.compare(expected)).overridingErrorMessage(errorMsg).isTrue();

        } catch (LangException exception) {
            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }
    }
}
