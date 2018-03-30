package com.bathem.vocabpower.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bathem.vocabpower.Activity.Base.BaseActivity;
import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Interface.IFileStatusListener;
import com.bathem.vocabpower.Manager.GoogleDriveManager;
import com.bathem.vocabpower.Manager.GoogleDriveManager2;
import com.bathem.vocabpower.R;


public class SettingsActivity extends BaseActivity {

    GoogleDriveManager mDriveManager;
    GoogleDriveManager2 mDriveManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDriveManager = GoogleDriveManager.getInstance();
        mDriveManager2 = GoogleDriveManager2.getInstance();
        initBackupButton();
        initRestoreButton();
        addProgressBarLoader(R.id.layout_activity_setting);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mSpinner == null) {
            mSpinner = (ProgressBar) findViewById(R.id.progressBar);
            mSpinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mDriveManager.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDriveManager.onStop();
    }

    void initBackupButton() {
        Button btn = (Button) findViewById(R.id.button_google_drive_backup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialogue("Backup Database", "This action will overwright existing database on drive.", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initBackup();
                    }
                }, null);
            }
        });
    }

    void initBackup() {
        mDriveManager.initGoogleClient(SettingsActivity.this, DriveMode.backup, fileListener);
        mDriveManager.connect();
        mSpinner.setVisibility(View.VISIBLE);
    }

    void initRestoreButton() {
        Button btn = (Button) findViewById(R.id.button_google_drive_restore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showConfirmationDialogue("Restore Database", "This action will overwright existing database.", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initRestore();
                    }
                }, null);

            }
        });
    }

    void initRestore() {
//        mDriveManager.initGoogleClient(SettingsActivity.this, DriveMode.restore, fileListener );
//        mDriveManager.connect();

        mDriveManager2.initGoogleClient(SettingsActivity.this, DriveMode.restore, fileListener);
        mSpinner.setVisibility(View.VISIBLE);
    }

    IFileStatusListener fileListener = new IFileStatusListener() {
        @Override
        public void onFileRestored() {
            mSpinner.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "Database has been restored.", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onFileUploaded() {
            mSpinner.setVisibility(View.GONE);
            Toast toast = Toast.makeText(getApplicationContext(), "Database has been backed up.", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onFileFailed(DriveMode mode) {
            String msg;

            if(mode == DriveMode.backup)
                msg = "Failed to upload database";
            else
                msg = "Failed to restore database";

            mSpinner.setVisibility(View.GONE);

            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mDriveManager.onActivityResult(requestCode,resultCode,data);
    }

}
