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
            return new NodeName(token.getStringValue());
        } else if(type == Token.Type.STRING) {
            return new NodeString(token.getStringValue());
        } else {
            // throw an error
        }

        return null;
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
