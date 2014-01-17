/*
 * Copyright 2014 Inderjit Gill
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

public class Alterable {
    public String mGenSym;
    public Node.Type mType;

    public int mInt;
    public float mFloat;
    public String mName;
    public String mString;
    public boolean mBoolean;

    public static Alterable fromNode(Node node, String genSym) {
        Alterable a = new Alterable();
        a.mType = node.getType();
        a.mGenSym = genSym;
        // todo: fill in appropriate variable
        return a;
    }
};

