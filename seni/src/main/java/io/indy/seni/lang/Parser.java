package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<Token> mTokens;

    public List<Node> parse(List<Token> tokens) {
        List<Node> nodes = new ArrayList<Node>();
        mTokens = tokens;

        do {
            nodes.add(consumeItem());
        } while(mTokens.size() > 0);
        
        return nodes;
    }

    // TODO: replace List<Tokens> with Queue<Tokens>
    private Node consumeItem() {
        if(mTokens.size() == 0) {
            // raise error?
        }

        Token token = mTokens.remove(0);

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
        NodeList node = new NodeList();
        Token token;
        while(true) {
            if(mTokens.size() == 0) {
                // raise error?
            }
            token = mTokens.get(0);
            if(token.getType() == Token.Type.LIST_END) {
                mTokens.remove(0);
                return node;
            } else {
                node.addChild(consumeItem());
            }
        }
    }
}
