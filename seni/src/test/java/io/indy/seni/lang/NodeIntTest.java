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

public class NodeIntTest {

    @Test
    public void testNodeInt() {
        NodeInt n = new NodeInt(42);

        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(n.getInt()).isEqualTo(42);
    }

    @Test
    public void testEq() {
        NodeInt n = new NodeInt(42);

        assertThat(n.eq(n)).isTrue();
        assertThat(n.eq(new NodeInt(42))).isTrue();

        assertThat(n.eq(new NodeInt(666))).isFalse();
    }

    @Test
    public void testScribe() {
        NodeInt n = new NodeInt(42);
        assertThat(n.scribe()).isEqualTo("42");
    }

    @Test
    public void testToString() {
        NodeInt n = new NodeInt(42);
        assertThat(n.toString()).isEqualTo("INT: 42");
    }
}
