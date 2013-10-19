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

public class ParserTest {
    @Test
    public void parseInt() {
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(42));

        List<Node> nodes = Parser.parse(tokens);
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

        List<Node> nodes = Parser.parse(tokens);
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

        List<Node> nodes = Parser.parse(tokens);
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

        List<Node> nodes = Parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        NodeName node = (NodeName)n;
        assertThat(node.getName()).isEqualTo(val);
    }

    @Test
    public void parseBoolean() {
        String val = "true";
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.NAME, val));

        List<Node> nodes = Parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
        NodeBoolean node = (NodeBoolean)n;
        assertThat(node.getBoolean()).isEqualTo(true);


        val = "false";
        tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.NAME, val));

        nodes = Parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.BOOLEAN);
        node = (NodeBoolean)n;
        assertThat(node.getBoolean()).isEqualTo(false);
    }

    @Test
    public void parseListAndInt() {
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(16));
        tokens.add(makeToken(61));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(99));

        List<Node> nodes = Parser.parse(tokens);
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

        List<Node> nodes = Parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        NodeList nl = (NodeList)n;
        assertThat(nl.getChildren().size()).isEqualTo(3);
    }

    @Test
    public void parseQuoteAbbreviation() {
        // '(42 38)    -> (quote (42 38))
        Queue<Token> tokens = new ArrayDeque<Token>();
        tokens.add(makeToken(Token.Type.QUOTE_ABBREVIATION));
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(42));
        tokens.add(makeToken(38));
        tokens.add(makeToken(Token.Type.LIST_END));

        List<Node> nodes = Parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.LIST);

        NodeList nl = (NodeList)n;
        assertThat(nl.getChildren().size()).isEqualTo(2);

        n = nl.getChild(0);
        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        NodeName nn = (NodeName)n;
        assertThat(nn.getName()).isEqualTo("quote");

        n = nl.getChild(1);
        assertThat(n.getType()).isEqualTo(Node.Type.LIST);
        nl = (NodeList)n;

        n = nl.getChild(0);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        NodeInt ni = (NodeInt)n;
        assertThat(ni.getInt()).isEqualTo(42);

        n = nl.getChild(1);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        ni = (NodeInt)n;
        assertThat(ni.getInt()).isEqualTo(38);
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
