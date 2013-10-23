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

import io.indy.seni.core.Colour;

public class NodeColour extends Node {

    private Colour mColour;

    public NodeColour(Colour c) {
        super();

        mType = Node.Type.COLOUR;
        mColour = c;
    }

    public Colour getColour() {
        return mColour;
    }

    public boolean eq(Node n) {
        if (n.mType != Node.Type.COLOUR) {
            return false;
        }

        NodeColour nodeColour = (NodeColour)n;
        return mColour.compare(nodeColour.mColour);
    }

}
