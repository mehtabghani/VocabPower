package com.bathem.vocabpower.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

import java.util.List;

public class RandomVocabFragment extends Fragment {

    Vocab mVocab;

    public RandomVocabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_random_vocab, container, false);
    }

    private void init() {
        mVocab = getRandomVocab();
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mVocab != null) {
            updateWordField(mVocab.getWord());
            updateMeaningField(mVocab.getMeaning());
        }
    }

    private Vocab getRandomVocab() {

        int id = 2;

        DataBaseHelper db = new DataBaseHelper(getContext());
        return db.getVocabByID(id);
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


}
