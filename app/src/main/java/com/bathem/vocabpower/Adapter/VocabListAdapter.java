package com.bathem.vocabpower.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.R;

import java.util.ArrayList;

/**
 * Created by mehtab on 08/04/16.
 */
public class VocabListAdapter extends ArrayAdapter<Word> {

    boolean shouldShowChekBox;

    public VocabListAdapter(Context context, ArrayList<Word> words) {
        super(context, 0, words);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Word word = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_vocab_listview, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.vocab_cell_label);
        tv.setText(word.getWord());

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.vocab_cell_checkBox);

        if(shouldShowChekBox) {
            cb.setVisibility(View.VISIBLE);

        } else {
            cb.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

   public void setVisibilityOfCheckBox(Boolean shouldShow) {
        this.shouldShowChekBox = shouldShow;
    }
}
