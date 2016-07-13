package com.bathem.vocabpower.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Fragment.DetailFragment;
import com.bathem.vocabpower.Fragment.ListFragment;
import com.bathem.vocabpower.Helper.StringUtil;
import com.bathem.vocabpower.R;

public class VocabListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    public static final String TAG_FRAMENT_LIST = "list_fragment";
    public static final String TAG_FRAMENT_DETAIL = "detail_fragment";
    boolean isDetailFragmentVisible;
    ListFragment listFragment;
    DetailFragment detailFragment;
    MenuItem mSearchItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_list);
        showListFragment();
        initAddVocabButton();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d("activity", "handleIntent");

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.d("activity", query);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String id = intent.getDataString();
            Log.d("activity", "Selected suggestion id: " + id);

            if(!StringUtil.stringEmptyOrNull(id)) {
                showDetailFragment(Integer.parseInt(id));
                MenuItemCompat.collapseActionView(mSearchItem);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);

        // Associate searchable configuration with the SearchView
        mSearchItem = menu.findItem(R.id.action_search_vocab);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, VocabListActivity.class)));
        searchView.setIconifiedByDefault(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_delete:
                onDeleteActionButtonPressed();
                return true;

            case R.id.action_edit:
                onEditActionButtonPressed();
                return true;

            case R.id.action_favourite:
                onFavouriteActionButtonPressed();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    void showListFragment() {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        listFragment = new ListFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_placeholder, listFragment, TAG_FRAMENT_LIST);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        isDetailFragmentVisible = false;
        showAddVocabButton(View.VISIBLE);
    }

    public void showDetailFragment(int id) {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        detailFragment = DetailFragment.newInstance(id);

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_placeholder, detailFragment, TAG_FRAMENT_DETAIL);

        // Complete the changes added above
        ft.commit();
        isDetailFragmentVisible = true;
        showAddVocabButton(View.INVISIBLE);

    }

    void initAddVocabButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_fab_list_add_vocab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VocabListActivity.this, AddVocabActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showAddVocabButton(int v) {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_fab_list_add_vocab);
        fab.setVisibility(v);
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

    void onDeleteActionButtonPressed() {
        if (listFragment != null) {
            listFragment.onDeleteActionButtonPressed();

        }
    }

    void onEditActionButtonPressed() {
        if (detailFragment != null) {
            detailFragment.onEditActionButtonPressed();
        }
    }

    void onFavouriteActionButtonPressed() {
        if (detailFragment != null) {
            detailFragment.onFavouriteActionButtonPressed();
        }
    }

    // Search action methods

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Log.d("activity", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

}


