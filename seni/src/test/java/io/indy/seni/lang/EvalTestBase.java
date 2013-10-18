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
import java.util.Queue;
import org.junit.Test;
import org.junit.Before;
import static org.fest.assertions.api.Assertions.assertThat;


abstract public class EvalTestBase {

    protected void assertEval(String code, String expected) {
        Node n = run(code);
        Node expectedAST = asAST(expected);
        String errorMsg = "eval: " + code + " != " + expected;
        assertThat(expectedAST.eq(n)).overridingErrorMessage(errorMsg).isTrue();
    }

    private Node asAST(String code) {
        Token t;
        Queue<Token> tokens;

        try {

            tokens = Lexer.tokenise(code);

            List<Node> nodes = Parser.parse(tokens);
            assertThat(nodes.size()).isEqualTo(1);
        
            return nodes.get(0);

        } catch (LangException exception) {
            assertThat(true).isFalse();
        }

        return null;
    }

    // runs single s-expressions
    private Node run(String code) {

        try {
            Node ast = asAST(code);
            Env e = Env.bindCoreFuns(new Env());

            return Interpreter.eval(e, ast);

        } catch (LangException exception) {
            assertThat(true).isFalse();
        }

        return null;
    }

}
