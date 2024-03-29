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

import java.util.ArrayList;
import java.util.List;

public abstract class NodeMutate extends Node {

    protected static String IN_RANGE = "in-range";

    protected boolean mAlterable;
    protected String mGenSym;

    protected List<Node> mParameterAst;

    public NodeMutate() {
        this(false);
    }

    public NodeMutate(boolean alterable) {
        mAlterable = alterable;

        if (mAlterable) {
            mParameterAst = new ArrayList<>();
        }
    }

    /**
     *
     * @return a mutated version of this node
     */
    abstract public NodeMutate mutate();

    /**
     *
     * @return a clone of this node
     */
    abstract public NodeMutate klone();

    /**
     *
     * @param val a string containing the unparsed value
     * @return a clone of this node but with a value of val
     */
    abstract public NodeMutate deviate(String val);

    /**
     *
     * @return a string value that can be given to another NodeMutate's deviate method
     */
    abstract public String asSerialisableString();


    public void setGenSym(String genSym) {
        mGenSym = genSym;
    }

    public String getGenSym() {
        return mGenSym;
    }

    @Override
    public boolean isAlterable() {
        return mAlterable;
    }

    public void addParameterNode(Node node) {
        mParameterAst.add(node);
    }

    public List<Node> getParameterNodes() {
        return mParameterAst;
    }

    @Override
    public String scribe(Env env) throws Node.ScribeException {
        if (isAlterable()) {

            Node n = this;
            try {
                if (env != null && env.hasBinding(mGenSym)) {
                    n = env.lookup(mGenSym);
                }
            } catch (LangException e) {
                throw new ScribeException("LangException->ScribeException " + e);
            }

            return "[" + n.scribeValue() + "]";
        } else {
            return scribeValue();
        }
    }
}
