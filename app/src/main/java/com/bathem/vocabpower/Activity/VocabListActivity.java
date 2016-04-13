package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Fragment.DetailFragment;
import com.bathem.vocabpower.Fragment.ListFragment;
import com.bathem.vocabpower.R;

public class VocabListActivity extends BaseActivity {

    public static final String TAG_FRAMENT_LIST = "list_fragment";
    public static final String TAG_FRAMENT_DETAIL = "detail_fragment";
    boolean isDetailFragmentVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_list);
        showListFragment();
        initAddVocabButton();
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.delete_vocab, menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.action_delete:
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
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.main_placeholder, new ListFragment(), TAG_FRAMENT_LIST);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        isDetailFragmentVisible = false;
        showAddVocabButton(View.VISIBLE);
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

    void showAddVocabButton (int v) {

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


}
