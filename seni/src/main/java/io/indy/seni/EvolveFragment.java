package io.indy.seni;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.indy.seni.dummy.DummyContent;

/**
 * A fragment representing a single Script detail screen.
 * This fragment is either contained in a {@link ScriptListActivity}
 * in two-pane mode (on tablets) or a {@link ScriptDetailActivity}
 * on handsets.
 */
public class EvolveFragment extends Fragment {
    /**
     * The original script that all other images are derived from
     */
    public static final String GENESIS_SCRIPT = "genesis_script";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EvolveFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(GENESIS_SCRIPT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(GENESIS_SCRIPT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_evolve, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.script_detail)).setText(mItem.content);
//        }

        return rootView;
    }
}
