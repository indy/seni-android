package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class LexerTest {

    @Test
    public void testTokenise() {
        List<Token> lt = Lexer.tokenise("()");

        assertThat(lt.size()).isEqualTo(2);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.LIST_END);


        lt = Lexer.tokenise("( (     )");
        assertThat(lt.size()).isEqualTo(3);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.LIST_END);


        lt = Lexer.tokenise("   (\"hello\"  )  ");
        assertThat(lt.size()).isEqualTo(3);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.STRING);
        assertThat(lt.get(1).getStringValue()).isEqualTo("hello");
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.LIST_END);


        lt = Lexer.tokenise("   (hello \"bob\"  )  ");
        assertThat(lt.size()).isEqualTo(4);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.NAME);
        assertThat(lt.get(1).getStringValue()).isEqualTo("hello");
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.STRING);
        assertThat(lt.get(2).getStringValue()).isEqualTo("bob");
        assertThat(lt.get(3).getType()).isEqualTo(Token.Type.LIST_END);


        lt = Lexer.tokenise("(hello 23 -54.3 \"bob\")");
        assertThat(lt.size()).isEqualTo(6);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);

        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.NAME);
        assertThat(lt.get(1).getStringValue()).isEqualTo("hello");

        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.INT);
        assertThat(lt.get(2).getIntValue()).isEqualTo(23);

        assertThat(lt.get(3).getType()).isEqualTo(Token.Type.FLOAT);
        assertThat(lt.get(3).getFloatValue()).isEqualTo(-54.3f);

        assertThat(lt.get(4).getType()).isEqualTo(Token.Type.STRING);
        assertThat(lt.get(4).getStringValue()).isEqualTo("bob");

        assertThat(lt.get(5).getType()).isEqualTo(Token.Type.LIST_END);
    }

}
