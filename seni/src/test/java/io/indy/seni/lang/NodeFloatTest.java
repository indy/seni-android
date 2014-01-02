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

package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class NodeFloatTest {

    @Test
    public void testNodeFloat() {
        NodeFloat n = new NodeFloat(12.34f);

        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        assertThat(n.getFloat()).isEqualTo(12.34f);
    }

    @Test
    public void testCastingToNodeFloat() {
        NodeInt n = new NodeInt(42);

        try {
            assertThat(Node.asFloatValue(n)).isEqualTo(42.0f);
        } catch(LangException e) {
            assertThat(false).isEqualTo(true);
        }
    }

    @Test
    public void testEq() {
        NodeFloat n = new NodeFloat(12.34f);

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeFloat(12.34f))).isTrue();

        assertThat(n.eq(new NodeFloat(98.76f))).isFalse();
    }

    @Test
    public void testScribe() {
        NodeFloat n = new NodeFloat(12.34f);
        assertThat(n.scribe()).isEqualTo("12.34");
    }

    @Test
    public void testToString() {
        NodeFloat n = new NodeFloat(12.34f);
        assertThat(n.toString()).isEqualTo("FLOAT: 12.34");
    }

}

