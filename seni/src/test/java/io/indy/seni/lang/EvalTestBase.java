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

    protected void assertBinding(String code, String name, String expected) {
        Node expectedAST = asAST(expected).get(0);

        Queue<Token> tokens;
        try {

            Env e = Env.bindCoreFuns(new Env());

            tokens = Lexer.tokenise(code);
            List<Node> ast = Parser.parse(tokens);
            for (Node node : ast) {
                Interpreter.eval(e, node);
            }

            Node n = e.lookup(name);

            String errorMsg = "binding " + name + " != " + expected;
            assertThat(expectedAST.eq(n)).overridingErrorMessage(errorMsg).isTrue();

        } catch (LangException e) {
            assertThat(true).overridingErrorMessage(e.toString()).isFalse();
        }
    }

    protected void assertEval(String code, String expected) {
        Node n = run(code);

        // assuming that expected evals to a single node
        Node expectedAST = asAST(expected).get(0);

        String errorMsg = "eval: " + code + " != " + expected;
        assertThat(expectedAST.eq(n)).overridingErrorMessage(errorMsg).isTrue();
    }

    private List<Node> asAST(String code) {
        Token t;
        Queue<Token> tokens;

        try {

            tokens = Lexer.tokenise(code);
            return Parser.parse(tokens);

        } catch (LangException exception) {
            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }

        return null;
    }

    // runs single s-expressions
    protected Node run(String code) {

        try {
            List<Node> ast = asAST(code);
            Env e = Env.bindCoreFuns(new Env());

            Node res = null;

            for (Node node : ast) {
                res = Interpreter.eval(e, node);
            }
            
            return res;

        } catch (LangException exception) {
            assertThat(true).overridingErrorMessage(exception.toString()).isFalse();
        }

        return null;
    }

}
