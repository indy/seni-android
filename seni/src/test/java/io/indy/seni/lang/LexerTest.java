package io.indy.seni.lang;

import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class LexerTest {

    @Test
    public void testWhitespace() {
        Lexer lex = new Lexer();
        assertThat(lex.isWhitespace(' ')).isTrue();
        assertThat(lex.isWhitespace('\t')).isTrue();
        assertThat(lex.isWhitespace('\n')).isTrue();

        assertThat(lex.isWhitespace('a')).isFalse();
        assertThat(lex.isWhitespace('0')).isFalse();
    }

    @Test
    public void testDigit() {
        Lexer lex = new Lexer();
        assertThat(lex.isDigit('0')).isTrue();
        assertThat(lex.isDigit('1')).isTrue();
        assertThat(lex.isDigit('2')).isTrue();
        assertThat(lex.isDigit('3')).isTrue();
        assertThat(lex.isDigit('4')).isTrue();
        assertThat(lex.isDigit('5')).isTrue();
        assertThat(lex.isDigit('6')).isTrue();
        assertThat(lex.isDigit('7')).isTrue();
        assertThat(lex.isDigit('8')).isTrue();
        assertThat(lex.isDigit('9')).isTrue();

        assertThat(lex.isDigit('a')).isFalse();
        assertThat(lex.isDigit('z')).isFalse();
    }

    @Test
    public void testSkipWhitespace() {
        Lexer lex = new Lexer();
        assertThat(lex.skipWhitespace("  hello")).isEqualTo("hello");
        assertThat(lex.skipWhitespace("    ")).isEqualTo("");
        assertThat(lex.skipWhitespace("foo  ")).isEqualTo("foo  ");
    }
    
    @Test
    public void testNextTokenType() {
        Lexer lex = new Lexer();
        assertThat(lex.nextTokenType("(foo")).isEqualTo(Token.Type.LIST_START);
        assertThat(lex.nextTokenType(") ")).isEqualTo(Token.Type.LIST_END);
        assertThat(lex.nextTokenType("\"hello")).isEqualTo(Token.Type.STRING);
    }

    @Test
    public void testConsumeListStart() {
        Lexer lex = new Lexer();
        Lexer.Pair p;

        p = lex.consumeListStart("(foo");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(p.mRemaining).isEqualTo("foo");
    }

    @Test
    public void testTokenise() {
        Lexer lex = new Lexer();

        List<Token> lt = lex.tokenise("()");

        assertThat(lt.size()).isEqualTo(2);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.LIST_END);


        lt = lex.tokenise("( (     )");
        assertThat(lt.size()).isEqualTo(3);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.LIST_END);


        lt = lex.tokenise("   (\"hello\"  )  ");
        assertThat(lt.size()).isEqualTo(3);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.STRING);
        assertThat(lt.get(1).getStringValue()).isEqualTo("hello");
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.LIST_END);


        lt = lex.tokenise("   (hello \"bob\"  )  ");
        assertThat(lt.size()).isEqualTo(4);
        assertThat(lt.get(0).getType()).isEqualTo(Token.Type.LIST_START);
        assertThat(lt.get(1).getType()).isEqualTo(Token.Type.NAME);
        assertThat(lt.get(1).getStringValue()).isEqualTo("hello");
        assertThat(lt.get(2).getType()).isEqualTo(Token.Type.STRING);
        assertThat(lt.get(2).getStringValue()).isEqualTo("bob");
        assertThat(lt.get(3).getType()).isEqualTo(Token.Type.LIST_END);


        lt = lex.tokenise("(hello 23 -54.3 \"bob\")");
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

    @Test
    public void testConsumeString() {
        Lexer lex = new Lexer();

        Lexer.Pair p = lex.consumeString("\"hello foo\" other stuff here");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.STRING);
        assertThat(p.mToken.getStringValue()).isEqualTo("hello foo");
        assertThat(p.mRemaining).isEqualTo(" other stuff here");
    }

    @Test
    public void testConsumeName() {
        Lexer lex = new Lexer();

        Lexer.Pair p = lex.consumeName("a-name other stuff here");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.NAME);
        assertThat(p.mToken.getStringValue()).isEqualTo("a-name");
        assertThat(p.mRemaining).isEqualTo(" other stuff here");

        p = lex.consumeName("a-33* other stuff here");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.NAME);
        assertThat(p.mToken.getStringValue()).isEqualTo("a-33*");
        assertThat(p.mRemaining).isEqualTo(" other stuff here");
    }

    @Test
    public void testConsumeInt() {
        Lexer lex = new Lexer();

        Lexer.Pair p = lex.consumeInt("234 foo bar");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.INT);
        assertThat(p.mToken.getIntValue()).isEqualTo(234);
        assertThat(p.mRemaining).isEqualTo(" foo bar");
    }

    @Test
    public void testConsumeFloat() {
        Lexer lex = new Lexer();

        Lexer.Pair p = lex.consumeFloat("3.14 foo bar");
        assertThat(p.mToken.getType()).isEqualTo(Token.Type.FLOAT);
        assertThat(p.mToken.getFloatValue()).isEqualTo(3.14f);
        assertThat(p.mRemaining).isEqualTo(" foo bar");
    }
}
