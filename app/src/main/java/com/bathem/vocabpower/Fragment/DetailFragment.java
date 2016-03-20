package com.bathem.vocabpower.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bathem.vocabpower.R;

/**
 * Created by mehtab on 16/03/16.
 */
public class DetailFragment extends Fragment {

    public static final String VOCAB_ID = "id";
    int id;

    public static DetailFragment newInstance(int id) {

        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VOCAB_ID, 0);
        fragment.setArguments(bundle);
        return  fragment;
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, parent, false);
    }



}
