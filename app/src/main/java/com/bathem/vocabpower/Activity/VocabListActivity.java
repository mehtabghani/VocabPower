package com.bathem.vocabpower.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.bathem.vocabpower.Fragment.DetailFragment;
import com.bathem.vocabpower.Fragment.ListFragment;
import com.bathem.vocabpower.R;

public class VocabListActivity extends AppCompatActivity {

    public static final String TAG_FRAMENT_LIST = "list_fragment";
    public static final String TAG_FRAMENT_DETAIL = "detail_fragment";
    boolean isDetailFragmentVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_list);
        showListFragment();
    }

    void showListFragment() {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_placeholder, new ListFragment(),TAG_FRAMENT_LIST);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        isDetailFragmentVisible = false;
    }

    public void showDetailFragment(int id) {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        DetailFragment detailFragment = DetailFragment.newInstance(id);

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_placeholder, detailFragment, TAG_FRAMENT_DETAIL);

        // Complete the changes added above
        ft.commit();
        isDetailFragmentVisible = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isDetailFragmentVisible) {
                showListFragment();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }




}
