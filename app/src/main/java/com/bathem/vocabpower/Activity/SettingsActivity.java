package com.bathem.vocabpower.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Manager.GoogleDriveManager;
import com.bathem.vocabpower.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SettingsActivity extends AppCompatActivity {

    GoogleDriveManager mDriveManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDriveManager = GoogleDriveManager.getInstance();
        initBackupButton();
    }

    void initBackupButton() {
        Button btn = (Button) findViewById(R.id.button_google_drive_backup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDriveManager.initGoogleClient(SettingsActivity.this);
                mDriveManager.connect();
            }
        });
    }

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
