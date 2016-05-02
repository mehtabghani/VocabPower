package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Enum.AddEditMode;
import com.bathem.vocabpower.Fragment.AddEditFragment;
import com.bathem.vocabpower.R;

public class AddVocabActivity extends BaseActivity {

    public static final String ADD_EDIT_MODE = "MODE";
    AddEditFragment mAddEditFragment;
    AddEditMode mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocab);
        Intent intent = getIntent();
        mode = (intent.getIntExtra(ADD_EDIT_MODE, 0) == 0)? AddEditMode.add_mode : AddEditMode.edit_mode;

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

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        this.mAddEditFragment = new AddEditFragment();
        this.mAddEditFragment.setMode(mode);
        ft.replace(R.id.add_edit_placeholder, this.mAddEditFragment, "");
        ft.commit();
    }

    void addVocab() {

        if(mAddEditFragment == null)
            return;

        if(mode == AddEditMode.add_mode)
            mAddEditFragment.addVocab();
        else
            mAddEditFragment.editVocab();
    }


}
