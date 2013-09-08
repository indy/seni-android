package io.indy.seni.lang;

import java.util.ArrayList;
import java.util.List;

public class NodeList extends Node {

    private List<Node> mChildren;

    public NodeList(Node parent) {
        super(parent);

        mType = Node.Type.LIST;
        mChildren = new ArrayList<Node>();
    }

    public void addChild(Node child) {
        mChildren.add(child);
    }

    public List<Node> getChildren() {
        return mChildren;
    }
}
