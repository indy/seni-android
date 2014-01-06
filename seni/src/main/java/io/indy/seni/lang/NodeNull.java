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

// NodeNull is returned in the following cases:
//
// (if false 1)   ;  conditional eval's to false but there's no false clause
// (begin)        ;  empty begin

// TODO: deal with the above cases and remove concept of NULL from seni

public class NodeNull extends Node {

    public NodeNull() {
        super();

        mType = Node.Type.NULL;
    }

    public boolean eq(Node n) {
        return (n.mType == Node.Type.NULL);
    }


    public String scribe() throws ScribeException {
        throw new ScribeException("NodeNull");
    }
}
