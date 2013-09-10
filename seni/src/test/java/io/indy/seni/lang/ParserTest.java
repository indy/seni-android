package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {
    @Test
    public void parseInt() {
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(42));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);
        
        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        NodeInt ni = (NodeInt)n;
        assertThat(ni.getInt()).isEqualTo(42);
    }

    @Test
    public void parseFloat() {
        float val = 42.5f;
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(val));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        NodeFloat node = (NodeFloat)n;
        assertThat(node.getFloat()).isEqualTo(val);
    }

    @Test
    public void parseString() {
        String val = "foo";
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.STRING, val));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.STRING);
        NodeString node = (NodeString)n;
        assertThat(node.getString()).isEqualTo(val);
    }

    @Test
    public void parseName() {
        String val = "foo";
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.NAME, val));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        NodeName node = (NodeName)n;
        assertThat(node.getName()).isEqualTo(val);
    }

    @Test
    public void parseListAndInt() {
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(16));
        tokens.add(makeToken(61));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(99));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(2);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        NodeList nl = (NodeList)n;
        assertThat(nl.getChildren().size()).isEqualTo(2);

        n = nodes.get(1);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
    }

    @Test
    public void parseNestedList() {
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(2));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(4));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(8));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(Token.Type.LIST_END));

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        NodeList nl = (NodeList)n;
        assertThat(nl.getChildren().size()).isEqualTo(3);
    }


    private Token makeToken(int val) {
        try {
            return new Token(Token.Type.INT, val);
        } catch(Token.TokenException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Token makeToken(float val) {
        try {
            return new Token(Token.Type.FLOAT, val);
        } catch(Token.TokenException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Token makeToken(Token.Type t) {
        return new Token(t);
    }

    private Token makeToken(Token.Type t, String val) {
        return new Token(t, val);
    }
}
