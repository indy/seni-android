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

public class Parser {

    private static Queue<Token> mTokens;

    public static List<Node> parse(Queue<Token> tokens) {

        List<Node> nodes = new ArrayList<Node>();
        mTokens = tokens;

        while(mTokens.peek() != null) {
            nodes.add(consumeItem());
        }
        
        return nodes;
    }

    private static Node consumeItem() {

        Token token = mTokens.remove();
        Token.Type type = token.getType();

        if(type == Token.Type.LIST_START) {
            return consumeList();
        } else if(type == Token.Type.INT) {
            return new NodeInt(token.getIntValue());
        } else if(type == Token.Type.FLOAT) {
            return new NodeFloat(token.getFloatValue());
        } else if(type == Token.Type.NAME) {
            String val = token.getStringValue();
            if (val.equals("true")) {
                return new NodeBoolean(true);
            } else if (val.equals("false")) {
                return new NodeBoolean(false);
            } else {
                return new NodeName(token.getStringValue());
            }
        } else if(type == Token.Type.STRING) {
            return new NodeString(token.getStringValue());
        } else if(type == Token.Type.QUOTE_ABBREVIATION) {
            return consumeQuotedForm();
        } else if(type == Token.Type.BRACKET_START) {
            return consumeBracketForm();
        } else {
            // throw an error
        }

        return null;
    }

    private static Node consumeBracketForm() {
        // [4] -> 4
        // [(foo bar)] -> (foo bar)

        // should only be one form in-between square brackets
        Node node = consumeItem();
        
        // mark node as having been surrounded by square brackets
        node.setAlterable(true);

        // after consuming it we should be on the end bracket
        Token token = mTokens.remove();
        if(token.getType() != Token.Type.BRACKET_END) {
            // throw an error - only one form allowed in-between brackets
        }

        return node;
    }

    private static Node consumeQuotedForm() {
        // '(2 3 4) -> (quote (2 3 4))
        
        NodeList node = new NodeList();

        node.addChild(new NodeName("quote"));
        node.addChild(consumeItem());
        
        return node;
    }

    private static Node consumeList() {
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
