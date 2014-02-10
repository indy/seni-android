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

package io.indy.seni.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.indy.seni.AppConfig;
import io.indy.seni.BuildConfig;
import io.indy.seni.R;
import io.indy.seni.SeniApp;
import io.indy.seni.adapter.EvolveAdapter;
import io.indy.seni.lang.Genotype;
import io.indy.seni.model.EvolveContainer;
import io.indy.seni.util.ImageCache;
import io.indy.seni.util.ImageGenerator;

public class EvolveGridFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "EvolveGridFragment";
    private static final boolean D = true;

    static void ifd(final String message) {
        if (AppConfig.DEBUG && D) Log.d(TAG, message);
    }

    public static final String GENESIS_SCRIPT = "GENESIS_SCRIPT";
    public static final String EVOLVE_CONTAINER = "EVOLVE_CONTAINER";

    private String mGenesisScript;

    private int mImageThumbSize;
    private int mImageThumbSpacing;

    private EvolveAdapter mAdapter;
    private EvolveContainer mEvolveContainer;
    private ImageGenerator mImageGenerator;

    private GridView mGridView;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public EvolveGridFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ifd("onCreate");

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams();

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageGenerator takes care of loading images into our ImageView children asynchronously
        mImageGenerator = new ImageGenerator(getActivity(), mImageThumbSize);
        mImageGenerator.setLoadingImage(R.drawable.empty_genotype);
        mImageGenerator.addImageCache(getActivity().getFragmentManager(), cacheParams);


        if (getArguments().containsKey(GENESIS_SCRIPT)) {
            // first time call
            mEvolveContainer = new EvolveContainer();
            mGenesisScript = getArguments().getString(EvolveGridFragment.GENESIS_SCRIPT);
            mEvolveContainer.setScript(mGenesisScript);
            mEvolveContainer.mutatePopulation(50);
        } else {
            //
            if(getArguments().containsKey(EVOLVE_CONTAINER)) {
                String json = getArguments().getString(EVOLVE_CONTAINER);
                mEvolveContainer = EvolveContainer.fromJsonString(json);
            } else {
                ifd("expected an EVOLVE_CONTAINER value");
            }
            // create 50 genotypes from the latest breeding population
            mEvolveContainer.breedPopulation(50);
        }

        mAdapter = new EvolveAdapter(getActivity(), mImageGenerator, mEvolveContainer);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_evolve, container, false);

        mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {

                } else {
                    mImageGenerator.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGridView.setMultiChoiceModeListener(mMultiChoiceModeListener);

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                                if (AppConfig.hasJellyBean()) {
                                    mGridView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                } else {
                                    mGridView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                }
                            }
                        }
                    }
                });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageGenerator.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageGenerator.setPauseWork(false);
        mImageGenerator.setExitTasksEarly(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        /*final Intent i = new Intent(getActivity(), ImageDetailActivity.class);
        i.putExtra(ImageDetailActivity.EXTRA_IMAGE, (int) id);
        if (Utils.hasJellyBean()) {
            // makeThumbnailScaleUpAnimation() looks kind of ugly here as the loading spinner may
            // show plus the thumbnail image in GridView is cropped. so using
            // makeScaleUpAnimation() instead.
            ActivityOptions options =
                    ActivityOptions.makeScaleUpAnimation(v, 0, 0, v.getWidth(), v.getHeight());
            getActivity().startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
        */
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cache:
                mImageGenerator.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Manage the multiple phenotype selections
     */
    private AbsListView.MultiChoiceModeListener mMultiChoiceModeListener =
            new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                    // Here you can do something when items are selected/de-selected,
                    // such as update the title in the CAB

                    ifd("onItemCheckedStateChanged");
                    int count = mGridView.getCheckedItemCount();
                    if(count == 1) {
                        mode.setTitle("1 phenotype selected");
                    } else  {
                        mode.setTitle("" + count + " phenotypes selected");
                    }
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // Respond to clicks on the actions in the CAB

                    switch (item.getItemId()) {
                        case R.id.menu_breed:

                            // launch a new EvolveGridFragment with the chosen genotypes
                            List<Genotype> genotypes = new ArrayList<>();
                            long[] ids = mGridView.getCheckedItemIds();
                            int pos, i;

                            for(i=0;i<ids.length;i++) {
                                pos = (int)ids[i];
                                Genotype genotype = mEvolveContainer.getGenotype(pos);
                                genotypes.add(genotype);
                            }

                            Bundle arguments = new Bundle();

                            mEvolveContainer.newGeneration(genotypes);
                            arguments.putString(EVOLVE_CONTAINER, mEvolveContainer.toJsonString());

                            mode.finish(); // Action picked, so close the CAB

                            EvolveGridFragment fragment = new EvolveGridFragment();
                            fragment.setArguments(arguments);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.evolve_container, fragment);
                            ft.commit();

                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    ifd("onCreateActionMode");

                    // Inflate the menu for the CAB
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.context_evolve, menu);

                    mode.setTitle("Choose the best");
                    mode.setSubtitle("generation 1");
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    // Here you can make any necessary updates to the activity when
                    // the CAB is removed. By default, selected items are deselected/unchecked.
                    ifd("onDestroyActionMode");
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // Here you can perform updates to the CAB due to
                    // an invalidate() request
                    ifd("onPrepareActionMode");
                    return false;
                }
            };
}
