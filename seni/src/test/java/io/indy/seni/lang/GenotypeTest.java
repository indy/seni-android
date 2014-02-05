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

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class GenotypeTest {

    @Test
    public void parseAlterableInt() {

        Genotype a = new Genotype(null);
        Genotype b = new Genotype(null);
        a.add(new NodeInt(1));
        b.add(new NodeInt(99));

        a.add(new NodeInt(2));
        b.add(new NodeInt(98));

        a.add(new NodeInt(3));
        b.add(new NodeInt(97));

        a.add(new NodeInt(4));
        b.add(new NodeInt(96));

        Genotype c;
        Node n;

        try {
            c = Genotype.crossover(a, b, 2);
            List<NodeMutate> alt = c.getAlterable();
            assertThat(Node.asIntValue(alt.get(0))).isEqualTo(1);
            assertThat(Node.asIntValue(alt.get(1))).isEqualTo(2);
            assertThat(Node.asIntValue(alt.get(2))).isEqualTo(97);
            assertThat(Node.asIntValue(alt.get(3))).isEqualTo(96);
        } catch (LangException e) {
            e.printStackTrace();
            assertThat(true).isEqualTo(false);
        }


    }
}
