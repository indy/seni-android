package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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

    public List<Node> getChildren() {
        return mChildren;
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

        if (myIter.hasNext()) {
            if (!myIter.next().eq(otherIter.next())) {
                return false;
            }
        }
        
        return true;
    }
}
