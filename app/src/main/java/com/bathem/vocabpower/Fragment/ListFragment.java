package com.bathem.vocabpower.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bathem.vocabpower.Activity.VocabListActivity;
import com.bathem.vocabpower.Adapter.VocabListAdapter;
import com.bathem.vocabpower.Constant.AppConstant;
import com.bathem.vocabpower.Entity.Word;
import com.bathem.vocabpower.Enum.SortType;
import com.bathem.vocabpower.Helper.SharedPreferenceHelper;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

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
         setHasOptionsMenu(true);

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
        prepareDropDown();
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
        showNoWordAvailableIfNeeded();
        adapter.notifyDataSetChanged();
    }

    void prepareDropDown () {

        Spinner dynamicSpinner = (Spinner) getActivity().findViewById(R.id.spinner_sort_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, AppConstant.SORT_LIST_ARRAY);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dynamicSpinner.setAdapter(adapter);
        String strType = SharedPreferenceHelper.getsInstance().getSharedPreferenceByKey(AppConstant.KEY_SORT_LIST_TYPE);
        dynamicSpinner.setSelection(adapter.getPosition(strType)); //set previously set value

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String type = (String) parent.getItemAtPosition(position);
                SharedPreferenceHelper.getsInstance().setSharedPreferenceByKey(AppConstant.KEY_SORT_LIST_TYPE, type);
                sortList(type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    void prepareListView() {

        words = (ArrayList<Word>) DataModel.getCurrentWordList();

        if(words ==  null) {
         Log.d("debug", "No words found");
            return;
        }

        initAdapter();

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




    private void initAdapter() {
        adapter = new VocabListAdapter(getActivity(), words);
        adapter.setVisibilityOfCheckBox(false);

        String strType = SharedPreferenceHelper.getsInstance().getSharedPreferenceByKey(AppConstant.KEY_SORT_LIST_TYPE);

        if(StringUtil.stringEmptyOrNull(strType))
            strType = AppConstant.SORT_LIST_ARRAY[0];

        sortList(strType);
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
                        return -1;
                    else if( lhs.getCreateAt().after(rhs.getCreateAt()) )
                        return 1;
                    else
                        return 0;
                }

                return col.compare(lhs.getWord(),rhs.getWord());
            }
        });
    }

    public void onDeleteActionButtonPressed() {

        if(adapter.getVisibilityOfCheckBox() == false) {
            adapter.setVisibilityOfCheckBox(true);
            adapter.notifyDataSetChanged();
            VocabListActivity activity = (VocabListActivity) getActivity();
            activity.showAddVocabButton(View.INVISIBLE);
        } else {
            //delete
            adapter.setVisibilityOfCheckBox(false);
            adapter.notifyDataSetChanged();
            deleteItems();
            VocabListActivity activity = (VocabListActivity) getActivity();
            activity.showAddVocabButton(View.VISIBLE);
        }
    }

    void deleteItems () {
        ArrayList<Integer> ids = adapter.getListOfItemsToBeDelete();

        if(ids != null && ids.size() > 0) {

            DataModel.deleteVocab(ids, getActivity());

            for (Integer id:ids) {
                Log.d("debug", "delete id:" + id);
            }

            refreshListView();
        } else {
            Log.d("debug", "None of the item selected.");
        }
        adapter.clearDeleteItemList();
    }

    void refreshListView () {

        DataModel.refreshWordList(getActivity());
        adapter.notifyDataSetChanged();
        showNoWordAvailableIfNeeded();
    }

    void showNoWordAvailableIfNeeded () {
        TextView textView = (TextView) getActivity().findViewById(R.id.textView_no_word_available);
        textView.setVisibility((words == null || words.size() <= 0) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(false);
        menu.findItem(R.id.action_favourite).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

}
