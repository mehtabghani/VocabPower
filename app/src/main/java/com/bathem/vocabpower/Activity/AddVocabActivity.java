package com.bathem.vocabpower.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Fragment.AddEditFragment;
import com.bathem.vocabpower.R;

public class AddVocabActivity extends BaseActivity {


    AddEditFragment mAddEditFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocab);

        showAddFragment();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_vocab, menu);

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add:
                addVocab();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    void showAddFragment() {

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        this.mAddEditFragment = new AddEditFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.add_edit_placeholder, this.mAddEditFragment, "");
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    void addVocab() {

        if(mAddEditFragment != null)
            mAddEditFragment.addVocab();
    }


}
