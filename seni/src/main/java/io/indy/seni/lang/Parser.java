package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Parser {

    private Queue<Token> mTokens;

    public List<Node> parse(Queue<Token> tokens) {

        List<Node> nodes = new ArrayList<Node>();
        mTokens = tokens;

        do {
            nodes.add(consumeItem());
        } while(mTokens.peek() != null);
        
        return nodes;
    }

    // TODO: replace List<Tokens> with Queue<Tokens>
    private Node consumeItem() {

        Token token = mTokens.remove();
        Token.Type type = token.getType();

        if(type == Token.Type.LIST_START) {
            return consumeList();
        } else if(type == Token.Type.INT) {
            return new NodeInt(token.getIntValue());
        } else if(type == Token.Type.FLOAT) {
            return new NodeFloat(token.getFloatValue());
        } else if(type == Token.Type.NAME) {
            return new NodeName(token.getStringValue());
        } else if(type == Token.Type.STRING) {
            return new NodeString(token.getStringValue());
        } else {
            // throw an error
        }

        return null;
    }

    private Node consumeList() {
        // LIST_START has already been consumed

        NodeList node = new NodeList();
        Token token;

        while(true) {
            token = mTokens.element();
            if(token.getType() == Token.Type.LIST_END) {
                mTokens.remove();
                return node;
            } else {
                node.addChild(consumeItem());
            }
        }

    }
}
