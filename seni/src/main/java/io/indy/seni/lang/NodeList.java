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
import java.util.Iterator;
import java.util.List;

public class NodeList extends Node {

    private List<Node> mChildren;

    public NodeList() {
        super();

        mType = Node.Type.LIST;
        mChildren = new ArrayList<Node>();
    }

    public void addChild(Node child) {
        mChildren.add(child);
    }

    public Node getChild(int nth) {
        return mChildren.get(nth);
    }

    public List<Node> getChildren() {
        return mChildren;
    }

    public int size() {
        return getChildren().size();
    }

    public boolean eq(Node n) {

        if (n.mType != Node.Type.LIST) {
            return false;
        }

        NodeList nl = (NodeList)n;

        if (mChildren.size() != nl.mChildren.size()) {
            return false;
        }

        Iterator<Node> myIter = mChildren.iterator();
        Iterator<Node> otherIter = nl.mChildren.iterator();

        while (myIter.hasNext()) {
            if (!myIter.next().eq(otherIter.next())) {
                return false;
            }
        }

        return true;
    }
}
