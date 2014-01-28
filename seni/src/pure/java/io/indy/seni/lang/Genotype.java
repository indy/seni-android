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

    // list of alterable nodes and their values for this instance
    private List<Node> mAlterable;

    public Genotype() {
        mAlterable = new ArrayList<Node>();
    }

    public List<Node> getAlterable() {
        return mAlterable;
    }

    public Env bind(Env env) {
        Env e = env.newScope();

        for (Node n : mAlterable) {
            e.addBinding(n.getGenSym(), n);
        }

        return e;
    }

    public void add(Node n) {
        mAlterable.add(n);
    }

    public Genotype mutate() {
        // temp
        Genotype genotype = new Genotype();

        for (Node n : mAlterable) {
            NodeFloat nf = new NodeFloat((float) Math.random());
            nf.setGenSym(n.getGenSym());
            genotype.add(nf);
        }

        return genotype;
    }

    public static Genotype crossover(Genotype a, Genotype b, int index) {
        Genotype g = new Genotype();

        if (a.mAlterable.size() != b.mAlterable.size()) {
            // todo: throw an error
            return null;
        }

        Iterator<Node> aIter = a.mAlterable.iterator();
        Iterator<Node> bIter = b.mAlterable.iterator();
        Node aNode, bNode;

        int count = 0;
        while (aIter.hasNext()) {
            aNode = aIter.next();
            bNode = bIter.next();
            g.add(count++ < index ? aNode : bNode);
        }
        return g;
    }
}
