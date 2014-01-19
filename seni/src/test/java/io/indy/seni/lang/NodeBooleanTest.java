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

public class NodeBooleanTest {

    @Test
    public void testNodeBoolean() {
        NodeBoolean n = new NodeBoolean(true);
        assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
        assertThat(n.getBoolean()).isTrue();

        NodeBoolean m = new NodeBoolean(false);
        assertThat(m.getType()).isEqualTo(Node.Type.BOOLEAN);
        assertThat(m.getBoolean()).isFalse();
    }

    @Test
    public void testEq() {
        NodeBoolean t = new NodeBoolean(true);
        NodeBoolean f = new NodeBoolean(false);

        assertThat(t.eq(t)).isTrue();
        assertThat(t.eq(new NodeBoolean(true))).isTrue();

        assertThat(t.eq(f)).isFalse();
    }

    @Test
    public void testToString() {
        NodeBoolean t = new NodeBoolean(true);
        NodeBoolean f = new NodeBoolean(false);
        assertThat(t.toString()).isEqualTo("BOOLEAN: true");
        assertThat(f.toString()).isEqualTo("BOOLEAN: false");
    }
}
