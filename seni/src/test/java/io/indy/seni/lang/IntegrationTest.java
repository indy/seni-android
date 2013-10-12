package io.indy.seni.lang;

import java.util.List;
import java.util.Queue;
import org.junit.Test;
import org.junit.Before;
import static org.fest.assertions.api.Assertions.assertThat;


public class IntegrationTest {

    @Test
    public void testMath() {
        assertIntExpression("(+ 2 1)", 3);
        assertIntExpression("(+ (+ 2 1) (+ 2 1))", 6);
        assertIntExpression("(* (* 2 2) (* 3 3))", 36);
    }

    @Test
    public void testSpecialForms() {
        assertIntExpression("((lambda (x) (+ x x)) 3)", 6);
    }

    private void assertIntExpression(String code, int val) {
        Node n = run(code);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        assertThat(((NodeInt)n).getInt()).isEqualTo(val);        
    }

    // runs single s-expressions
    private Node run(String code) {
        Token t;
        Queue<Token> tokens;

        try {

            tokens = Lexer.tokenise(code);

            List<Node> nodes = Parser.parse(tokens);
            assertThat(nodes.size()).isEqualTo(1);
        
            Node ast = nodes.get(0);
            Env e = Env.bindCoreFuns(new Env());

            return Interpreter.eval(e, ast);

        } catch (LangException exception) {
            assertThat(true).isEqualTo(false);
        }

        return null;
    }

}
