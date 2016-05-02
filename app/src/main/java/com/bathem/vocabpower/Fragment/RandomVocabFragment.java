package com.bathem.vocabpower.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.util.List;


public class RandomVocabFragment extends Fragment {

    Vocab mVocab;
    boolean wordLayoutVisible;


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


    @Override
    public void onStart() {
        super.onStart();

        initRandomVocabButton();
        initWordMeaningToggleButton();
        addClickListenerOnRandomContainer();

        if(mVocab != null) {
            updateFields();
            hideMeaningLayout();
        }
    }

    private void init() {
        getRandomVocab();
    }

    private void updateFields() {
        updateWordField(mVocab.getWord().getWord());
        updateMeaningField(mVocab.getMeaning());
    }

    private void getRandomVocab() {
        mVocab = DataModel.getCurrentRandomVocab();
    }

    private void initRandomVocabButton() {
        Button btn = (Button) getActivity().findViewById(R.id.button_random_vocab);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVocab = DataModel.getRandomVocab(getContext());
                updateFields();
            }
        });
    }

    private void initWordMeaningToggleButton() {

        Button btn = (Button) getActivity().findViewById(R.id.button_toogle_word_meaning);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWordMeaningView();
            }
        });
    }

    private void addClickListenerOnRandomContainer() {
        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.layout_random_container);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWordMeaningView();
            }
        });
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

    private void toggleWordMeaningView() {
        if(wordLayoutVisible) {
            hideWordLayout();
            showMeaningLayout();
        } else {
            showWordLayout();
            hideMeaningLayout();
        }
    }

    private void hideMeaningLayout() {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_random_meaning);
        layout.setVisibility(View.GONE);
    }

    private void hideWordLayout() {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_random_word);
        layout.setVisibility(View.GONE);
        wordLayoutVisible = false;
    }

    private void showMeaningLayout() {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_random_meaning);
        layout.setVisibility(View.VISIBLE);
    }

    private void showWordLayout() {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layout_random_word);
        layout.setVisibility(View.VISIBLE);
        wordLayoutVisible = true;
    }
}
