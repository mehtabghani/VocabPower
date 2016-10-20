package com.bathem.vocabpower.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Fragment.FavouriteListFragment;
import com.bathem.vocabpower.R;

public class FavouriteActivity extends BaseActivity {


    FavouriteListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        init();
    }

    public void init() {
        showListFragment();
        setTitle("Favourites");
    }

    void showListFragment() {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(listFragment == null)
            listFragment = new FavouriteListFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.frame_favourite_list, listFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

        isDetailFragmentVisible = false;
    }

    public void showDetailFragment(int id) {
        super.showDetailFragment(id, R.id.frame_favourite_list);
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
