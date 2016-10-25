package com.bathem.vocabpower.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bathem.vocabpower.Activity.FavouriteActivity;
import com.bathem.vocabpower.Adapter.FavouriteListAdapter;
import com.bathem.vocabpower.Constant.AppConstant;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Enum.SortType;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteListFragment extends Fragment  {

    FavouriteListAdapter adapter;
    ArrayList<Word> words;

    public FavouriteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_list, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prepareListView();
    }


    @Override
    public void onResume() {
        super.onResume();
        words = (ArrayList<Word>) DataModel.getFavouriteWordsList(getActivity());
       // String type = SharedPreferenceHelper.getInstance().getSharedPreferenceByKey(AppConstant.KEY_SORT_LIST_TYPE);
       // sortList(type);
        if(words != null && words.size() > 0)
            adapter.notifyDataSetChanged();
    }

    void prepareListView() {

        words = (ArrayList<Word>) DataModel.getFavouriteWordsList(getActivity());

        if(words ==  null || words.size() <= 0) {
            Log.d("debug", "No words found");
            showNoDataAvailable(true);
            return;
        }
        showNoDataAvailable(false);

        initAdapter();

        ListView listView = (ListView)getView().findViewById(R.id.vocab_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word word = adapter.getItem(position);
                Log.d("word", "Word ID:" + word.getId());

                if (word != null) {
                    FavouriteActivity actvity = (FavouriteActivity) getHost();
                    actvity.showDetailFragment(word.getId());
                }
            }
        });
    }

    private void initAdapter() {
        adapter = new FavouriteListAdapter(getActivity(), words, this);
       // adapter.setVisibilityOfCheckBox(false);

       // String strType = SharedPreferenceHelper.getInstance().getSharedPreferenceByKey(AppConstant.KEY_SORT_LIST_TYPE);

       // if(StringUtil.stringEmptyOrNull(strType))
        //    strType = AppConstant.SORT_LIST_ARRAY[0];

        sortList(AppConstant.SORT_LIST_ARRAY[1]);
    }

    private SortType getSortType(String type) {

        if(type.equals(AppConstant.SORT_LIST_ARRAY[1]))
            return SortType.byDate;

        return SortType.alphabetically;
    }

    private void sortList(final String strType) {

        if(adapter == null) return;

        final SortType type = getSortType(strType);

        final Collator col = Collator.getInstance();

        adapter.sort(new Comparator<Word>() {
            @Override
            public int compare(Word lhs, Word rhs) {

                if (type == SortType.byDate){
                    if(lhs.getCreateAt().before(rhs.getCreateAt()))
                        return 1;
                    else if( lhs.getCreateAt().after(rhs.getCreateAt()) )
                        return -1;
                    else
                        return 0;

                }

                return col.compare(lhs.getWord(),rhs.getWord());
            }
        });
    }

    public void onDeleteItem(Word word) {
        word.setFavourite(false);
        DataModel.updateWord(word, getContext());

        if(words ==  null || words.size() <= 0) {
            showNoDataAvailable(true);
        }
    }


    void showNoDataAvailable(boolean show) {
       TextView tv = (TextView) getActivity().findViewById(R.id.textView_no_word_available);
        if(show)
            tv.setVisibility(View.VISIBLE);
        else
            tv.setVisibility(View.GONE);

    }
}
