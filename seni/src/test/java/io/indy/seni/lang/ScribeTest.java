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

package io.indy.seni.lang;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ScribeTest extends EvalTestBase {

    @Test
    public void testScribe() {
        NodeBoolean t = new NodeBoolean(true);
        NodeBoolean f = new NodeBoolean(false);
        try {
            assertThat(t.scribe(null)).isEqualTo("true");
            assertThat(f.scribe(null)).isEqualTo("false");
        } catch (Node.ScribeException e) {
            assertThat(false).isEqualTo(true);
        }
    }

    @Test
    public void scribeSimple() {

        String script = "(+ [4] [5])";
        AstHolder astHolder = new AstHolder(script);
        Genotype genotype = astHolder.getGenotype();

        assertScribe(astHolder, genotype, script);
    }

    @Test
    public void scribeInts() {

        AstHolder astHolder = new AstHolder("(+ [4] [5])");
        Genotype genotype = astHolder.getGenotype();

        Genotype derived = new Genotype(null);
        appendAlterable(genotype, derived, new NodeInt(7));
        appendAlterable(genotype, derived, new NodeInt(9));

        assertScribe(astHolder, derived, "(+ [7] [9])");
    }


    @Test
    public void scribeFloats() {

        AstHolder astHolder = new AstHolder("(+ [3.14] [6.9])");
        Genotype genotype = astHolder.getGenotype();

        Genotype derived = new Genotype(null);
        appendAlterable(genotype, derived, new NodeFloat(5.43f));
        appendAlterable(genotype, derived, new NodeFloat(9.32f));

        assertScribe(astHolder, derived, "(+ [5.43] [9.32])");
    }

    @Test
    public void scribeNames() {

        AstHolder astHolder = new AstHolder("(+ 1 2 [foo])");
        Genotype genotype = astHolder.getGenotype();

        Genotype derived = new Genotype(null);
        appendAlterable(genotype, derived, new NodeName("bar"));

        assertScribe(astHolder, derived, "(+ 1 2 [bar])");
    }

    @Test
    public void scribeStrings() {

        AstHolder astHolder = new AstHolder("(+ 1 2 (fn [\"hi\"]) foo)");
        Genotype genotype = astHolder.getGenotype();

        Genotype derived = new Genotype(null);
        appendAlterable(genotype, derived, new NodeString("bye"));

        assertScribe(astHolder, derived, "(+ 1 2 (fn [\"bye\"]) foo)");
    }

    @Test
    public void scribeMixed() {

        AstHolder astHolder = new AstHolder("(+ [1] 2 (fn [\"hi\"]) [foo])");
        Genotype genotype = astHolder.getGenotype();

        Genotype derived = new Genotype(null);
        appendAlterable(genotype, derived, new NodeInt(42));
        appendAlterable(genotype, derived, new NodeString("bye"));
        appendAlterable(genotype, derived, new NodeName("bar"));

        assertScribe(astHolder, derived, "(+ [42] 2 (fn [\"bye\"]) [bar])");
    }
}
