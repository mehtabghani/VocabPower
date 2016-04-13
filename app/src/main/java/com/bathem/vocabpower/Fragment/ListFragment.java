package com.bathem.vocabpower.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bathem.vocabpower.Activity.VocabListActivity;
import com.bathem.vocabpower.Adapter.VocabListAdapter;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.util.ArrayList;

/**
 * Created by mehtab on 16/03/16.
 */
public class ListFragment extends Fragment {

    VocabListAdapter adapter;
    ArrayList<Word> words;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocab_list, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prepareListView();
    }

    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        words = (ArrayList<Word>) DataModel.getCurrentWordList();
        adapter.notifyDataSetChanged();
    }

    void prepareListView() {

        words = (ArrayList<Word>) DataModel.getCurrentWordList();

        if(words ==  null) {
         Log.d("debug", "No words found");
            return;
        }

        adapter = new VocabListAdapter(getActivity(), words);
        adapter.setVisibilityOfCheckBox(false);
        ListView listView = (ListView)getView().findViewById(R.id.vocab_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = DataModel.getCurrentWordList().get(position);
                Log.d("word", "Word ID:" + word.getId());

                if (word != null) {
                   VocabListActivity actvity = (VocabListActivity) getHost();
                    actvity.showDetailFragment(word.getId());
                }
            }
        });
    }

}
