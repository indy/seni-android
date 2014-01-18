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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class AstHolderTest {
    @Test
    public void parseAlterableInt() {

        AstHolder astHolder = new AstHolder("(+ [4] [5])");
        Genotype genotype = astHolder.getGenotype();

        List<Node> alterable = genotype.getAlterable();

        assertThat(alterable.size()).isEqualTo(2);
        Node n;

        try {
            n = alterable.get(0);
            assertThat(Node.asIntValue(n)).isEqualTo(4);
            assertThat(n.getGenSym()).isEqualTo("$_42");
        } catch(LangException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }

        try {
            n = alterable.get(1);
            assertThat(Node.asIntValue(n)).isEqualTo(5);
            assertThat(n.getGenSym()).isEqualTo("$_43");
        } catch(LangException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void parseNestedAlterableExpression() {

        AstHolder astHolder = new AstHolder("([rotate] (- [3.14] (+ [4] 5)))");
        Genotype genotype = astHolder.getGenotype();

        List<Node> alterable = genotype.getAlterable();


        assertThat(alterable.size()).isEqualTo(3);
        Node n;

        try {

            n = alterable.get(0);
            assertThat(Node.asNameValue(n)).isEqualTo("rotate");
            assertThat(n.getGenSym()).isEqualTo("$_42");

            n = alterable.get(1);
            assertThat(Node.asFloatValue(n)).isEqualTo(3.14f);
            assertThat(n.getGenSym()).isEqualTo("$_43");

            n = alterable.get(2);
            assertThat(Node.asIntValue(n)).isEqualTo(4);
            assertThat(n.getGenSym()).isEqualTo("$_44");

        } catch(LangException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }
    }

    @Test
    public void checkAlterableBindings() {

        AstHolder astHolder = new AstHolder("(+ [4] [5])");
        Env env = new Env();

        Genotype genotype = astHolder.getGenotype();
        Env res = genotype.bind(env);

        Node n;

        try {
            n = res.lookup("$_42");
            assertThat(Node.asIntValue(n)).isEqualTo(4);

            n = res.lookup("$_43");
            assertThat(Node.asIntValue(n)).isEqualTo(5);
        } catch(LangException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }

    }
}
