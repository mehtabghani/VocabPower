package com.bathem.vocabpower.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Fragment.FavouriteListFragment;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

import java.util.ArrayList;

/**
 * Created by mehtab on 20/10/2016.
 */
public class FavouriteListAdapter extends ArrayAdapter<Word>  {

    FavouriteListFragment mFragment;
    ArrayList<Word> mWords;

    public FavouriteListAdapter(Context context, ArrayList<Word> words, FavouriteListFragment fragment) {
        super(context, 0, words);
        mFragment = fragment;
        mWords = words;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Word word = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favourite_listview, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.vocab_cell_label);
        tv.setText(StringUtil.capitalizeFirstLetter( word.getWord() ));

        Button btnDelete = (Button) convertView.findViewById(R.id.delete_fav_button);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "FavouriteListAdapter -> delete button pressed.");
                mFragment.onDeleteItem(word);
                mWords.remove(position);
                notifyDataSetChanged();

            }
        });

        return convertView;

    }


}
