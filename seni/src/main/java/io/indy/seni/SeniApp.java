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

package io.indy.seni;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.ObjectGraph;
import io.indy.seni.lang.Genotype;

public class SeniApp extends Application {


    private static final String TAG = "SeniApp";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
//      Timber.plant(new DebugTree());
        } else {
            // TODO Crashlytics.start(this);
            // TODO Timber.plant(new CrashlyticsTree());
        }

        buildObjectGraphAndInject();

    }

    public void buildObjectGraphAndInject() {
        long start = System.nanoTime();

        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        ifd("Global object graph creation took " + diff + "ms");
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static SeniApp get(Context context) {
        return (SeniApp) context.getApplicationContext();
    }


    private List<Genotype> mBreedingGenotypes;
    private String mGenesisScript;

    public void setBreedingGenotypes(List<Genotype> genotypes) {
        mBreedingGenotypes = genotypes;
    }

    public List<Genotype> getBreedingGenotypes() {
        return mBreedingGenotypes;
    }

    public void setGenesisScript(String script) {
        mGenesisScript = script;
    }

    public String getGenesisScript() {
        return mGenesisScript;
    }
}
