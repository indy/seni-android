package io.indy.seni.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.Genotype;
import io.indy.seni.lang.NodeMutate;

public class Generation {

    private static final String TAG = "Generation";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    static private final String GENOTYPE = "genotype";
    static private final String BREEDING_POPULATION = "breedingPopulation";
    static private final String SEED = "seed";


    private List<Genotype> mBreedingGenotypes;
    private int mSeed;

    public Generation(List<Genotype> breedingGenotypes, int seed) {
        mBreedingGenotypes = breedingGenotypes;
        mSeed = seed;
    }

    public List<Genotype> getBreedingGenotypes() {
        return mBreedingGenotypes;
    }

    public int getSeed() {
        return mSeed;
    }

    /**
     *
     * @param population the desired number of genotypes to create
     * @return an array of genotypes
     */
    public Genotype[] breed(int population) {

        int i;
        Genotype[] genotypes = new Genotype[population];

        int breedSize = mBreedingGenotypes.size();
        int size = breedSize > population ? population : breedSize;

        // clone the breeding genotypes into this generation
        ifd("size = " + size);
        for(i=0;i<size;i++) {
            genotypes[i] = mBreedingGenotypes.get(i);
        }

        // generate the rest from the breeding population
        int geneLength = mBreedingGenotypes.get(0).getAlterable().size();
        ifd("geneLength = " + geneLength);

        for (i = size; i < population; i++) {
            int crossoverIndex = (int) (Math.random() * (double) geneLength);
            int a = (int)(Math.random() * (double)breedSize);
            int b = (int)(Math.random() * (double)breedSize);

            ifd("" + i + " breeding a(" + a + ") b(" + b + ") crossoverIndex(" + crossoverIndex + ")");

            genotypes[i] = Genotype.crossover(mBreedingGenotypes.get(a),mBreedingGenotypes.get(b),
                    crossoverIndex);
        }

        // overwrite the last 10% of the population with mutations
        int numMutants = (int)((float)population * 0.1f);
        for(i=population - numMutants;i<population;i++) {
            genotypes[i] = genotypes[0].mutate();
        }

        return genotypes;
    }


    public JSONObject toJson() {
        JSONObject res = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();
            for(Genotype genotype : getBreedingGenotypes()) {
                jsonArray.put(genotypeToJson(genotype));
            }

            res.put(BREEDING_POPULATION, jsonArray);
            res.put(SEED, getSeed());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }


    static public Generation fromJson(JSONObject jsonObject, Genotype template) {
        try {
            int seed = jsonObject.getInt(SEED);

            JSONArray jsonArray = jsonObject.getJSONArray(BREEDING_POPULATION);

            List<Genotype> vals = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++) {
                vals.add(jsonToGenotype(jsonArray.getJSONObject(i), template));
            }

            return new Generation(vals, seed);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    static private JSONObject genotypeToJson(Genotype genotype) {

        JSONObject res = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();
            List<NodeMutate> alleles = genotype.getAlterable();
            for(NodeMutate nodeMutate : alleles) {
                jsonArray.put(nodeMutate.asSerialisableString());
            }
            res.put(GENOTYPE, jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }

    static private Genotype jsonToGenotype(JSONObject jsonObject, Genotype template) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(GENOTYPE);
            List<String> vals = new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++) {
                vals.add(jsonArray.getString(i));
            }

            return template.deviate(vals);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Genotype.GenotypeMismatchException e) {
            ifd("");
            e.printStackTrace();
        }

        return null;
    }

}
