package com.bathem.vocabpower.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

import java.util.ArrayList;

/**
 * Created by mehtab on 08/04/16.
 */
public class VocabListAdapter extends ArrayAdapter<Word> {

    boolean shouldShowChekBox;
    ArrayList<Integer> listOfItemsToBeDelete;
    Handler listHandler;


    public VocabListAdapter(Context context, ArrayList<Word> words) {
        super(context, 0, words);
        this.listOfItemsToBeDelete = new ArrayList<Integer>();
        setVisibilityOfCheckBox(false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Word word = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vocab_listview, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.vocab_cell_label);
        tv.setText(StringUtil.capitalizeFirstLetter( word.getWord() ));

       final CheckBox cb = (CheckBox) convertView.findViewById(R.id.vocab_cell_checkBox);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cb.isChecked()) {
                    listOfItemsToBeDelete.add(Integer.valueOf( word.getId() ));
                } else {
                    listOfItemsToBeDelete.remove(Integer.valueOf( word.getId() ));
                }
            }
        });

        if(shouldShowChekBox) {
            cb.setVisibility(View.VISIBLE);
            cb.setChecked(false);

        } else {
            cb.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setVisibilityOfCheckBox(Boolean shouldShow) {
        this.shouldShowChekBox = shouldShow;
    }

    public boolean getVisibilityOfCheckBox() {
        return this.shouldShowChekBox;
    }

    public ArrayList<Integer> getListOfItemsToBeDelete () {
        return listOfItemsToBeDelete;
    }

    public void clearDeleteItemList() {
        listOfItemsToBeDelete.clear();
    }


}
