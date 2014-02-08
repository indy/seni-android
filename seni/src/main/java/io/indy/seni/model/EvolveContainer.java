package io.indy.seni.model;

import android.util.Log;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;

/**
 * Holds the model used by EvolveAdapter
 */
public class EvolveContainer {

    private static final String TAG = "EvolveContainer";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private List<Genotype> mBreedingGenotypes;
    private AstHolder mAstHolder;
    private Genotype[] mGenotypes;
    private int mPopulation;

    public EvolveContainer() {
        mPopulation = 50;
        mBreedingGenotypes = null;
    }

    public int getPopulation() {
        return mPopulation;
    }

    public Genotype getGenotype(int index) {
        return mGenotypes[index];
    }

    public void setScript(String script) {
        mAstHolder = new AstHolder(script);
    }

    public void setBreedingGenotypes(List<Genotype> genotypes) {
        mBreedingGenotypes = genotypes;
    }

    public void breed(int desiredPopulation) {

        int i;
        mPopulation = desiredPopulation;
        mGenotypes = new Genotype[mPopulation];

        // no breeding genotypes, everything is a mutation
        if(mBreedingGenotypes == null) {
            for (i = 0; i < mPopulation; i++) {
                mGenotypes[i] = mAstHolder.getGenotype().mutate();
            }
            return;
        }

        int breedSize = mBreedingGenotypes.size();
        int size = breedSize > mPopulation ? mPopulation : breedSize;

        // clone the breeding genotypes into this generation
        ifd("size = " + size);
        for(i=0;i<size;i++) {
            mGenotypes[i] = mBreedingGenotypes.get(i);
        }

        // generate the rest from the breeding population
        int geneLength = mBreedingGenotypes.get(0).getAlterable().size();
        ifd("geneLength = " + geneLength);

        for (i = size; i < mPopulation; i++) {
            int crossoverIndex = (int) (Math.random() * (double) geneLength);
            int a = (int)(Math.random() * (double)breedSize);
            int b = (int)(Math.random() * (double)breedSize);

            ifd("" + i + " breeding a(" + a + ") b(" + b + ") crossoverIndex(" + crossoverIndex + ")");

            mGenotypes[i] = Genotype.crossover(mBreedingGenotypes.get(a),mBreedingGenotypes.get(b),
                    crossoverIndex);
        }
    }

}
