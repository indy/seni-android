package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {
    @Test
    public void parseInt() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(42));

        assertThat(tokens.size()).isEqualTo(1);

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);
        
        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.INT);
        NodeInt ni = (NodeInt)n;
        assertThat(ni.getInt()).isEqualTo(42);
    }

    @Test
    public void testConsumeItemWithFloat() {
        float val = 42.5f;
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(val));

        assertThat(tokens.size()).isEqualTo(1);

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.FLOAT);
        NodeFloat node = (NodeFloat)n;
        assertThat(node.getFloat()).isEqualTo(val);
    }

    @Test
    public void testConsumeItemWithString() {
        String val = "foo";
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(Token.Type.STRING, val));

        assertThat(tokens.size()).isEqualTo(1);

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.STRING);
        NodeString node = (NodeString)n;
        assertThat(node.getString()).isEqualTo(val);
    }

    @Test
    public void testConsumeItemWithName() {
        String val = "foo";
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(Token.Type.NAME, val));

        assertThat(tokens.size()).isEqualTo(1);

        Parser parser = new Parser();
        List<Node> nodes = parser.parse(tokens);
        assertThat(nodes.size()).isEqualTo(1);

        Node n = nodes.get(0);
        assertThat(n.getType()).isEqualTo(Node.Type.NAME);
        NodeName node = (NodeName)n;
        assertThat(node.getName()).isEqualTo(val);
    }

    @Test
    public void testConsumeItemWithListAndInt() {
        List<Token> tokens = new ArrayList<Token>();
        tokens.add(makeToken(Token.Type.LIST_START));
        tokens.add(makeToken(16));
        tokens.add(makeToken(61));
        tokens.add(makeToken(Token.Type.LIST_END));
        tokens.add(makeToken(99));

        assertThat(tokens.size()).isEqualTo(5);

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
    public void testConsumeItemWithNestedList() {
        List<Token> tokens = new ArrayList<Token>();
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

        assertThat(tokens.size()).isEqualTo(11);

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

/*

(ns seni.rt.lang.test.parser
  (:use;*CLJSBUILD-REMOVE*;-macros
    [clip-test.core;*CLJSBUILD-REMOVE*;-cljs
     :only [deftest testing is
            ;*CLJSBUILD-REMOVE*;thrown? thrown-with-msg?
            ]])
  (:require [seni.rt.lang.parser :as parser]))

(deftest consume-item
  (is (= [nil '() 23]
         (parser/consume-item '([:number 23]))))
  (is (= [nil '([:number 23]) "foo"]
         (parser/consume-item '([:string "foo"] [:number 23])))))

(deftest consume-list
  (is (= [nil '() []]
         (parser/consume-list '([:list-start] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil nil]
         (parser/consume-list '([:list-start]))))

  (is (= [nil '() [23]]
         (parser/consume-list '([:list-start] [:number 23] [:list-end]))))

  (is (= ["error: not a list" '([:number 23] [:list-end]) nil]
         (parser/consume-list '([:number 23] [:list-end]))))

  (is (= [nil '() [23 "foo"]]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-end]))))

  (is (= [nil '() [23 "foo" [33 "bar"]]]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil nil]
         (parser/consume-list '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end])))))


(deftest parse
  ;; parser/parse returns [err result] vector
  (is (= [nil [[]]]
         (parser/parse '([:list-start] [:list-end]))))

  (is (= [nil [[23 "foo" [33 "bar"]]]]
         (parser/parse '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end] [:list-end]))))

  (is (= ["error: mismatched parentheses" nil]
         (parser/parse '([:list-start] [:number 23] [:string "foo"] [:list-start] [:number 33] [:string "bar"] [:list-end]))))

  )

(defn test-ns-hook
  []
  (consume-item)
  (consume-list)
  (parse))


 */
