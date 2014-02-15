package io.indy.seni.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.lang.Node;
import io.indy.seni.lang.NodeMutate;

/**
 * Holds the model used by EvolveAdapter
 */
public class EvolveContainer {

    private static final String TAG = "EvolveContainer";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private static final String GENESIS_SCRIPT = "script";
    private static final String GENESIS_SEED = "seed";
    private static final String POPULATION = "population";
    private static final String GENERATIONS = "generations";

    // the seed used to create the initial population of mutations
    private int mGenesisSeed;
    private List<Generation> mGenerations;
    private AstHolder mAstHolder;

    private int mPopulation;
    private Genotype[] mGenotypes;

    public EvolveContainer() {
        mPopulation = 60;
        mGenerations = new ArrayList<>();
    }

    public void setScript(String script) {
        mAstHolder = new AstHolder(script);
    }

    public void setGenesisSeed(int seed) {
        mGenesisSeed = seed;
    }

    public void setPopulation(int population) {
        mPopulation = population;
    }

    public int getPopulation() {
        return mPopulation;
    }

    public Genotype getGenotype(int index) {
        return mGenotypes[index];
    }

    public int getGenerationCount() {
        return mGenerations.size() + 1;
    }

    public void mutatePopulation() {
        Genotype proto = mAstHolder.getGenotype();
        mGenotypes = new Genotype[mPopulation];
        int i;

        mGenesisSeed = 42; // todo: pass this into mutate

        // everything is a mutation of proto
        for (i = 0; i < mPopulation; i++) {
            mGenotypes[i] = proto.mutate();
        }
    }

    public void newGeneration(List<Genotype> breedingGenotypes) {
        mGenerations.add(new Generation(breedingGenotypes, 43));
    }

    public void breedPopulation() {

        int numGenerations = mGenerations.size();

        Generation latestGen = mGenerations.get(numGenerations - 1);
        mGenotypes = latestGen.breed(mPopulation);

        JSONObject jsonObject = latestGen.toJson();
        ifd("jsonObject is: " + jsonObject.toString());

//        JSONObject jsonObject = GenotypeSerializer.genotypeToJson(mGenotypes[0]);
//        ifd("jsonObject is: " + jsonObject.toString());
    }

    public String toJsonString() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        JSONObject res = new JSONObject();

        try {
            res.put(GENESIS_SCRIPT, mAstHolder.scribe());
            res.put(GENESIS_SEED, mGenesisSeed);
            res.put(POPULATION, mPopulation);

            JSONArray jsonArray = new JSONArray();
            for(Generation generation : mGenerations) {
                jsonArray.put(generation.toJson());
            }
            res.put(GENERATIONS, jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Node.ScribeException se) {
            ifd("node scribe exception");
            se.printStackTrace();
        }

        return res;
    }

    static public EvolveContainer fromJsonString(String json) {
        try {
            return fromJson(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public EvolveContainer fromJson(JSONObject jsonObject) {
        try {

            EvolveContainer ec = new EvolveContainer();

            String script = jsonObject.getString(GENESIS_SCRIPT);
            ec.setScript(script);

            int population = jsonObject.getInt(POPULATION);
            ec.setPopulation(population);

            int seed = jsonObject.getInt(GENESIS_SEED);
            ec.setGenesisSeed(seed);

            Genotype template = ec.mAstHolder.getGenotype();

            JSONArray jsonArray = jsonObject.getJSONArray(GENERATIONS);
            for(int i=0;i<jsonArray.length();i++) {
                Generation g = Generation.fromJson(jsonArray.getJSONObject(i), template);
                ec.mGenerations.add(g);
            }

            return ec;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
