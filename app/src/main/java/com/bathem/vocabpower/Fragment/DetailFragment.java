package com.bathem.vocabpower.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

import java.util.List;

/**
 * Created by mehtab on 16/03/16.
 */
public class DetailFragment extends Fragment {

    public static final String VOCAB_ID = "id";
    int mID;
    Vocab mVocab;

    public static DetailFragment newInstance(int id) {

        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VOCAB_ID, id);
        fragment.setArguments(bundle);
        return  fragment;
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, parent, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get back arguments
        mID = getArguments().getInt(VOCAB_ID, 0);
        getData();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateFields();
    }

    private void getData() {
        //getvocab from db
        DataBaseHelper db = new DataBaseHelper(getContext());
        mVocab = db.getVocabByID(mID);
    }

    private void updateFields() {
        updateWordField(mVocab.getWord());
        updateMeaningField(mVocab.getMeaning());
        updateExampleField(mVocab.getExample());
    }

    private void updateWordField (String word) {
        Log.d("VOCAB", "Word:" + word);

        TextView tvWord = (TextView) getActivity().findViewById(R.id.textView_detail_word);
        tvWord.setText(StringUtil.capitalizeFirstLetter(word));
    }

    private void updateMeaningField (List<String> meanings) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 1;

        for (String str: meanings) {
            String txt = StringUtil.capitalizeFirstLetter(str);
            stringBuilder.append(index + ". "+ txt);
            Log.d("VOCAB", "Meaning:" + index + ". " + txt);

            if(meanings.size() > index)
                stringBuilder.append("\n\n");

            index++;
        }
        TextView tvMeaning = (TextView) getActivity().findViewById(R.id.textView_detail_meaning);
        tvMeaning.setText(stringBuilder.toString());
    }

    private void updateExampleField (List<String> examples) {

        StringBuilder stringBuilder = new StringBuilder();
        int index = 1;

        for (String str: examples) {
            String txt = StringUtil.capitalizeFirstLetter(str);
            stringBuilder.append(index + ". " + txt);
            Log.d("VOCAB", "Example:" + index + ". " + txt);
            stringBuilder.append("\n\n");
            index++;
        }

        TextView tvMeaning = (TextView) getActivity().findViewById(R.id.textView_detail_example);
        tvMeaning.setText(stringBuilder.toString());
    }

}
