package com.bathem.vocabpower.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Interface.IFileStatusListener;
import com.bathem.vocabpower.Manager.GoogleDriveManager;
import com.bathem.vocabpower.R;


public class SettingsActivity extends AppCompatActivity {

    GoogleDriveManager mDriveManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDriveManager = GoogleDriveManager.getInstance();
        initBackupButton();
        initRestoreButton();
    }

    void initBackupButton() {
        Button btn = (Button) findViewById(R.id.button_google_drive_backup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDriveManager.initGoogleClient(SettingsActivity.this, DriveMode.backup, fileListener);
                mDriveManager.connect();
            }
        });
    }

    void initRestoreButton() {
        Button btn = (Button) findViewById(R.id.button_google_drive_restore);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDriveManager.initGoogleClient(SettingsActivity.this, DriveMode.restore, fileListener );
                mDriveManager.connect();
            }
        });
    }

    IFileStatusListener fileListener = new IFileStatusListener() {
        @Override
        public void onFileRestored() {
            Toast toast = Toast.makeText(getApplicationContext(), "Database has been restored.", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onFileUploaded() {
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

            Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mDriveManager.onActivityResult(requestCode,resultCode,data);
    }

        @Override
    protected void onResume() {
        super.onResume();
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

}
