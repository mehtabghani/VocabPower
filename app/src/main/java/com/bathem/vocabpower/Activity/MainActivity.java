package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Entity.Vocab;
import com.bathem.vocabpower.Fragment.DetailFragment;
import com.bathem.vocabpower.Fragment.RandomVocabFragment;
import com.bathem.vocabpower.Helper.JSONHelper;
import com.bathem.vocabpower.Model.DataModel;
import com.bathem.vocabpower.R;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {
    RandomVocabFragment mRandomFragment;
    DetailFragment mDetailFragment;
    boolean isDetailFragmentVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_setting:
                showSettingsActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMainView();
    }

    void init() {
        refreshMainView();
        initListButton();
        initAddButton();
       // initSettingsButton();
    }

    void refreshMainView() {
        DataModel.getWordList(this);
        addRandomVocabFragment();
    }

    void addRandomVocabFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(mRandomFragment == null)
            mRandomFragment = new RandomVocabFragment();

        if(getRandomVocab() == null) {
            Log.d("debug", "No random vocab available.");
            setPaddingOfRandomFrameLayout(200);
            ft.remove(mRandomFragment);
        } else {
            setPaddingOfRandomFrameLayout(10);
            ft.replace(R.id.frameLayout_random_vocab, mRandomFragment);
        }
        ft.commit();
    }

    void setPaddingOfRandomFrameLayout (int bottomPadding) {
        FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout_random_vocab);
        layout.setPadding(10, 10, 10, bottomPadding);
    }

    private Vocab getRandomVocab() {

       return DataModel.getRandomVocab(getApplicationContext());
    }

    private void initListButton() {
        Button btnList = (Button) findViewById(R.id.button_list);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(DataModel.getWordList(MainActivity.this).size() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please add vocab first.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, VocabListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initAddButton() {
        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.button_fab_add_vocab);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddVocabActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void initSettingsButton() {
//        Button btnSetting = (Button) findViewById(R.id.button_settings);
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }

    void showSettingsActivity() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    void loadJSONFile () {

        try {
            String jsonData = JSONHelper.jsonToStringFromAssetFolder("vocabs.json", MainActivity.this);
            Log.d("json", jsonData);
            List<Vocab> vocabs = JSONHelper.getCollectionFromJSON(jsonData);

            if(vocabs != null)
                DataModel.setsVocabs(vocabs);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDetailFragment(int wordId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mDetailFragment = DetailFragment.newInstance(wordId);
        ft.replace(R.id.layout_main, mDetailFragment);
        ft.commit();
        isDetailFragmentVisible = true;
        setTitle("Detail");
    }

    public void removeDetailFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mDetailFragment);
        ft.commit();
        isDetailFragmentVisible = false;
        setTitle(R.string.app_name);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isDetailFragmentVisible) {
                removeDetailFragment();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


}
