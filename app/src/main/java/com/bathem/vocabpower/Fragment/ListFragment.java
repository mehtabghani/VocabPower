package com.bathem.vocabpower.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bathem.vocabpower.Activity.VocabListActivity;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehtab on 16/03/16.
 */
public class ListFragment extends Fragment {


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


    void prepareListView() {

        List<Word> words = DataModel.getWords();

        if(words ==  null) {
            DataBaseHelper db = new DataBaseHelper(getActivity().getApplicationContext());
            words = db.getWordList();//DataModel.getVocabs();
            DataModel.setWords(words);
        }

        List<String> list = new ArrayList<String>();

        for (Word v: words) {
            list.add( v.getWord() );
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_vocab_listview, list);
        ListView listView = (ListView)getView().findViewById(R.id.vocab_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = DataModel.getWords().get(position);
                Log.d("word", "Word ID:" + word.getId());

                if (word != null) {
                   VocabListActivity actvity = (VocabListActivity) getHost();
                    actvity.showDetailFragment(word.getId());
                }
            }
        });
    }

}
