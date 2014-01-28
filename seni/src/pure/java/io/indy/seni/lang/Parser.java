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

    public static List<Node> parse(Queue<Token> tokens) {

        List<Node> nodes = new ArrayList<Node>();

        while (tokens.peek() != null) {
            nodes.add(consumeItem(tokens, false));
        }

        return nodes;
    }

    private static Node consumeItem(Queue<Token> tokens, boolean alterable) {

        Token token = tokens.remove();
        Token.Type type = token.getType();

        if (type == Token.Type.LIST_START) {
            return consumeList(tokens, alterable);
        } else if (type == Token.Type.INT) {
            return new NodeInt(token.getIntValue(), alterable);
        } else if (type == Token.Type.FLOAT) {
            return new NodeFloat(token.getFloatValue(), alterable);
        } else if (type == Token.Type.NAME) {
            String val = token.getStringValue();
            if (val.equals("true")) {
                return new NodeBoolean(true, alterable);
            } else if (val.equals("false")) {
                return new NodeBoolean(false, alterable);
            } else {
                return new NodeName(token.getStringValue(), alterable);
            }
        } else if (type == Token.Type.STRING) {
            return new NodeString(token.getStringValue(), alterable);
        } else if (type == Token.Type.QUOTE_ABBREVIATION) {
            return consumeQuotedForm(tokens);
        } else if (type == Token.Type.BRACKET_START) {
            return consumeBracketForm(tokens);
        } else {
            // throw an error
        }

        return null;
    }

    private static Node consumeBracketForm(Queue<Token> tokens) {
        // [4] -> 4
        // [(foo bar)] -> (foo bar)

        // should only be one form in-between square brackets
        Node node = consumeItem(tokens, true);

        // after consuming it we should be on the end bracket
        Token token = tokens.remove();
        if (token.getType() != Token.Type.BRACKET_END) {
            // throw an error - only one form allowed in-between brackets
        }

        return node;
    }

    private static Node consumeQuotedForm(Queue<Token> tokens) {
        // '(2 3 4) -> (quote (2 3 4))

        NodeList node = new NodeList();

        node.addChild(new NodeName("quote"));
        node.addChild(consumeItem(tokens, false));

        return node;
    }

    private static Node consumeList(Queue<Token> tokens, boolean alterable) {
        // LIST_START has already been consumed

        NodeList node = new NodeList(alterable);
        Token token;

        while (true) {
            token = tokens.element();
            if (token.getType() == Token.Type.LIST_END) {
                tokens.remove();
                return node;
            } else {
                node.addChild(consumeItem(tokens, false));
            }
        }

    }
}
