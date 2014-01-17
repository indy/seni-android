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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class SymbolGeneratorTest {

    @Test
    public void symboGen() {
        SymbolGenerator sg = new SymbolGenerator();
        assertThat(sg.gen()).isEqualTo("$_42");
        assertThat(sg.gen()).isEqualTo("$_43");
    }

    @Test
    public void symboGen2() {
        SymbolGenerator sg = new SymbolGenerator(100);
        assertThat(sg.gen()).isEqualTo("$_100");
        assertThat(sg.gen()).isEqualTo("$_101");
    }

}
