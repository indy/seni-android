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

package io.indy.seni.adapter;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.lang.AstHolder;
import io.indy.seni.lang.Genotype;
import io.indy.seni.ui.RecyclingImageView;
import io.indy.seni.util.ImageGenerator;

public class EvolveAdapter extends BaseAdapter {

    private static final String TAG = "EvolveAdapter";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }


    private final Context mContext;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private int mActionBarHeight = 0;
    private GridView.LayoutParams mImageViewLayoutParams;

    private ImageGenerator mImageGenerator;

    private List<Genotype> mBreedingGenotypes;

    private AstHolder mAstHolder;
    private Genotype[] mGenotypes;
    private int mPopulation;

    public void setScript(String script) {
        mAstHolder = new AstHolder(script);
    }

    public void setBreedingGenotypes(List<Genotype> genotypes) {

        ifd("has genotypes: " + genotypes.size());

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
            ifd("i " + i);
            mGenotypes[i] = mBreedingGenotypes.get(i);
        }

        // generate the rest from the breeding population
        int geneLength = mBreedingGenotypes.get(0).getAlterable().size();

        for (i = size; i < mPopulation; i++) {
            int crossoverIndex = (int) (Math.random() * (double) geneLength);
            int a = (int)(Math.random() * (double)breedSize);
            int b = (int)(Math.random() * (double)breedSize);

            ifd("" + i + " breeding a(" + a + ") b(" + b + ") crossoverIndex(" + crossoverIndex + ")");

            mGenotypes[i] = Genotype.crossover(mBreedingGenotypes.get(a),mBreedingGenotypes.get(b),
                    crossoverIndex);
        }
    }

    public EvolveAdapter(Context context, ImageGenerator imageGenerator) {
        super();

        // The ImageGenerator takes care of loading images into our ImageView children asynchronously
        mImageGenerator = imageGenerator;

        mPopulation = 50;
        mBreedingGenotypes = null;

        mContext = context;
        mImageViewLayoutParams = new GridView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(
                android.R.attr.actionBarSize, tv, true)) {
            mActionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics());
        }
    }

    @Override
    public int getCount() {
        // If columns have yet to be determined, return no items
        if (getNumColumns() == 0) {
            return 0;
        }

        // Size + number of columns for top empty row
//            return Images.imageThumbUrls.length + mNumColumns;
        return mGenotypes.length + mNumColumns;
    }

    public Genotype getItemFromId(long itemId) {
        return mGenotypes[(int)itemId];
    }

    @Override
    public Object getItem(int position) {
        return position < mNumColumns ?
                null : mGenotypes[position - mNumColumns];
    }

    @Override
    public long getItemId(int position) {
        return position < mNumColumns ? 0 : position - mNumColumns;
    }

    @Override
    public int getViewTypeCount() {
        // Two types of views, the normal ImageView and the top row of empty views
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (position < mNumColumns) ? 1 : 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        // First check if this is the top row
        if (position < mNumColumns) {
            if (convertView == null) {
                convertView = new View(mContext);
            }
            // Set empty view with height of ActionBar
            convertView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mActionBarHeight));
            return convertView;
        }

        // Now handle the main ImageView thumbnails
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, instantiate and initialize
            imageView = new RecyclingImageView(mContext);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(mImageViewLayoutParams);

        } else { // Otherwise re-use the converted view
            imageView = (ImageView) convertView;
        }

        // Check the height matches our calculated column width
        if (imageView.getLayoutParams().height != mItemHeight) {
            imageView.setLayoutParams(mImageViewLayoutParams);
        }

        // Finally load the image asynchronously into the ImageView, this also takes care of
        // setting a placeholder image while the background thread runs
        mImageGenerator.loadImage(mGenotypes[position - mNumColumns], imageView);

        return imageView;
    }

    /**
     * Sets the item height. Useful for when we know the column width so the height can be set
     * to match.
     *
     * @param height
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams =
                new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
        mImageGenerator.setImageSize(height);
        notifyDataSetChanged();
    }

    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }
}
