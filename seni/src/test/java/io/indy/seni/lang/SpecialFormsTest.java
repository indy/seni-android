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

import org.junit.Test;

public class SpecialFormsTest extends EvalTestBase {

    @Test
    public void testSpecialForms() {
        assertEval("((lambda (x) (+ x x)) 3)", "6");

        assertEval("(if (< 12 77) (+ 1 3) (+ 1 7))", "4");
        assertEval("(if (< 12 77) 4 8)", "4");

        assertEval("(begin (+ 1 1) (+ 2 2) (+ 3 3))", "6");

        assertEval("(quote (1 2 3))", "(1 2 3)");
    }
}
