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

package io.indy.seni.lang.bind;

import org.junit.Test;
import io.indy.seni.lang.EvalTestBase;


public class ListsTest extends EvalTestBase {

    @Test
    public void testFirst() {
        assertEval("(first '(1 2 3))", "1");
    }

    @Test
    public void testSecond() {
        assertEval("(second '(1 2 3))", "2");
    }

    @Test
    public void testNth() {
        assertEval("(nth 0 '(1 2 3))", "1");
        assertEval("(nth 1 '(1 2 3))", "2");
        assertEval("(nth 2 '(1 2 3))", "3");
    }

    @Test
    public void testCons() {
        assertEval("(cons 42 '())", "(42)");
        assertEval("(cons 0 '(1 2 3))", "(0 1 2 3)");
        assertEval("(define foo '(1 2 3)) (cons 0 foo)", "(0 1 2 3)");
    }

    @Test
    public void testConcat() {
        assertEval("(concat '(1 2) '(3 4))", "(1 2 3 4)");
    }

    @Test
    public void testCount() {
        assertEval("(count '(9 8 7 6))", "4");
        assertEval("(count '())", "0");
    }

    @Test
    public void testMapcat() {
        assertEval("(mapcat (lambda (x) (cons x '())) '(1 2 3))",
                   "(1 2 3)");

        assertEval("(mapcat (lambda (x) (cons x '(8))) '(1 2 3))",
                   "(1 8 2 8 3 8)");

        assertEval("(mapcat (lambda (x y) (cons (+ x y) '(42))) '(1 2 3) '(4 5 6))",
                   "(5 42 7 42 9 42)");
    }

    @Test
    public void testReverse() {
        assertEval("(reverse '())", "()");
        assertEval("(reverse '(1 2))", "(2 1)");
        assertEval("(reverse '(1 2 3 4))", "(4 3 2 1)");
    }
}
