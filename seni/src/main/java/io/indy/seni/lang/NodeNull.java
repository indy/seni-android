package io.indy.seni.lang;

public class NodeNull extends Node {

    public NodeNull() {
        super();

        mType = Node.Type.NULL;
    }

    public boolean eq(Node n) {
        return (n.mType == Node.Type.NULL);
    }
}
