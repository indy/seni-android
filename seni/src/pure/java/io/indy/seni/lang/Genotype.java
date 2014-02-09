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
import java.util.Iterator;
import java.util.List;

public class Genotype {

    static public int sIdCount = 0;
    static public String createId() {
        return "genoId-" + Integer.toString(sIdCount++);
    }

    public class GenotypeMismatchException extends Exception {
        public GenotypeMismatchException(String message) {
            super(message);
        }
    }

    private String mId;
    private AstHolder mAstHolder;

    // list of alterable nodes and their values for this instance
    private List<NodeMutate> mAlterable;

    public Genotype(AstHolder astHolder) {
        mAstHolder = astHolder;
        mAlterable = new ArrayList<>();
        mId = Genotype.createId();
    }

    /**
     *
     * @param vals a list of strings containing unparsed values
     * @return a copy of this genotype but with the alterable list made up parsed vals
     */
    public Genotype deviate(List<String> vals) throws GenotypeMismatchException {

        if(vals.size() != mAlterable.size()) {
            throw new GenotypeMismatchException("vals size of " +
                    vals.size() + " expected " + mAlterable.size());
        }

        Genotype res = new Genotype(mAstHolder);
        int i = 0;
        NodeMutate nm;
        for(String val : vals) {
            nm = mAlterable.get(i++);
            res.add(nm.deviate(val));
        }

        return res;
    }

    public List<NodeMutate> getAlterable() {
        return mAlterable;
    }

    public String getId() {
        return mId;
    }

    public AstHolder getAstHolder() {
        return mAstHolder;
    }

    public Env bind(Env env) {
        Env e = env.newScope();

        for (NodeMutate n : mAlterable) {
            e.addBinding(n.getGenSym(), n);
        }

        return e;
    }

    public void add(NodeMutate n) {
        mAlterable.add(n);
    }

    public Genotype mutate() {
        // temp
        Genotype genotype = new Genotype(mAstHolder);

        for (NodeMutate n : mAlterable) {
            genotype.add(n.mutate());
        }

        return genotype;
    }

    public static Genotype crossover(Genotype a, Genotype b, int index) {
        Genotype g = new Genotype(a.mAstHolder);

        if (a.mAlterable.size() != b.mAlterable.size()) {
            // todo: throw an error
            return null;
        }

        Iterator<NodeMutate> aIter = a.mAlterable.iterator();
        Iterator<NodeMutate> bIter = b.mAlterable.iterator();
        NodeMutate aNode, bNode;

        int count = 0;
        while (aIter.hasNext()) {
            aNode = aIter.next();
            bNode = bIter.next();
            g.add(count++ < index ? aNode : bNode);
        }
        return g;
    }
}
