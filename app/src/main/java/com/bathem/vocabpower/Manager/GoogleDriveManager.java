package com.bathem.vocabpower.Manager;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by mehtab on 5/29/16.
 */
public class GoogleDriveManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = "GoogleDrive";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String DATABASE_NAME = DataBaseHelper.DATABASE_NAME;
    private static final String GOOGLE_DRIVE_FILE_NAME = "VocabMasterBackup.db";
    private static final String GOOGLE_DRIVE_FOLDER_NAME = "Vocab Master";

    private static GoogleDriveManager mManager;
    private DriveFile mfile;
    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private DriveId mIDDriveDBFile;
    private DriveId mIDDriveDBFolder;


    public static GoogleDriveManager getInstance() {

      if(mManager == null) {
          mManager = new GoogleDriveManager();
      }

       return mManager;
    }


    public void initGoogleClient(Activity activity) {
        mActivity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    //callbacks

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "API client connected.");
       // checkIfFileExist(GOOGLE_DRIVE_FILE_NAME);
        checkIfFileExist(GOOGLE_DRIVE_FOLDER_NAME);

    }

    void checkIfFileExist(final String fileName) {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, fileName))
                .build();

        Drive.DriveApi.query(mGoogleApiClient, query).setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
            @Override
            public void onResult(DriveApi.MetadataBufferResult result) {
                boolean isFileExist = false;

                if (!result.getStatus().isSuccess()) {
                    Log.v(TAG, "Error while trying to search DB file " + result.getStatus().getStatusMessage());
                    return;
                }

                for (Metadata m : result.getMetadataBuffer()) {
                    if (m.getTitle().equals(fileName)) {
                        Log.v(TAG, m.getTitle() + " file exist");
                        isFileExist = true;
                        //deleteFile(m.getDriveId());
                        deleteFolder(m.getDriveId());
                        break;
                    }
                }

                if (!isFileExist) {
                    createDBFile();
                    Log.v(TAG, fileName + " doesn't exist");
                }
            }
        });
    }

    void deleteFile(DriveId id) {

        DriveFile driveFile = Drive.DriveApi.getFile(mGoogleApiClient, id);
        //file.getMetadata(mGoogleApiClient).setResultCallback(metadataRetrievedCallback);
        driveFile.delete(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
               if(status.getStatus().isSuccess()) {
                   Log.d(TAG, "file deleted.");
               }
            }
        });
    }

    void deleteFolder(DriveId id) {

        DriveFolder driveFolder = Drive.DriveApi.getFolder(mGoogleApiClient, id);
        driveFolder.delete(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if(status.getStatus().isSuccess()) {
                    Log.d(TAG, "folder deleted.");
                    createDBFile();
                }
            }
        });
    }

    void createDBFile() {

        createFolder(GOOGLE_DRIVE_FOLDER_NAME);
    }


    void createFolder(final String folderName) {
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(folderName).build();
        Drive.DriveApi.getRootFolder(mGoogleApiClient).createFolder(
                mGoogleApiClient, changeSet).setResultCallback(new ResultCallback<DriveFolder.DriveFolderResult>() {
            @Override
            public void onResult(DriveFolder.DriveFolderResult driveFolderResult) {
                if (!driveFolderResult.getStatus().isSuccess()) {
                    Log.d(TAG, "Error while trying to create the folder");
                    return;
                }

                Log.d(TAG, folderName+ " folder created.");
                mIDDriveDBFolder = driveFolderResult.getDriveFolder().getDriveId();
                Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(contentsCallback);

            }
        });
    }

    final private ResultCallback<DriveApi.DriveContentsResult> contentsCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {

                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG, "Error while trying to create new file contents");
                        return;
                    }

                    createFile(result);
                }
            };


    void createFile(DriveApi.DriveContentsResult result) {

        String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                .setTitle(GOOGLE_DRIVE_FILE_NAME) // Google Drive File name
                .setMimeType(mimeType)
                .setStarred(true).build();

        //creat file inside folder
        Drive.DriveApi.getFolder(mGoogleApiClient, mIDDriveDBFolder).createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                .setResultCallback(fileCallback);
        
        // create a file on root folder

//        Drive.DriveApi.getRootFolder(mGoogleApiClient)
//                .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
//                .setResultCallback(fileCallback);


//        Drive.DriveApi.getAppFolder(mGoogleApiClient)
//                .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
//                .setResultCallback(fileCallback);
    }

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {

                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG, "Error while trying to create the file");
                        return;
                    }
                    mfile = result.getDriveFile();
                    mfile.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(contentsOpenedCallback);
                    mIDDriveDBFile = result.getDriveFile().getDriveId();
                    Log.d(TAG, "Google Drive File ID:" + mIDDriveDBFile);
                }
            };

    final private ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new
            ResultCallback<DriveApi.DriveContentsResult>() {

                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    if (!result.getStatus().isSuccess()) {
                        Log.v(TAG, "Error opening file");
                        return;
                    }

                    try {
                        File file = getDbPath();

                        if(!file.exists()) {
                            Log.v(TAG, "DB file does not exist.");
                            return;
                        }

                        FileInputStream is = new FileInputStream(file);
                        BufferedInputStream in = new BufferedInputStream(is);
                        byte[] buffer = new byte[8 * 1024];
                        DriveContents content = result.getDriveContents();
                        BufferedOutputStream out = new BufferedOutputStream(content.getOutputStream());
                        int n = 0;
                        while( ( n = in.read(buffer) ) > 0 ) {
                            out.write(buffer, 0, n);
                        }

                        in.close();
                        result.getDriveContents().commit(mGoogleApiClient, null)
                                .setResultCallback(new ResultCallback<Status>() {

                                    @Override
                                    public void onResult(Status result) {
                                        // Handle the response status
                                        Log.d(TAG, "Uploading status: " + result.getStatus());
                                        Toast toast = Toast.makeText(mActivity.getApplicationContext(), "Successfully backed up.", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            };

    private File getDbPath() {
        File file = mActivity.getDatabasePath(DATABASE_NAME);
        Log.d(TAG, "DB path: " + file.getAbsolutePath());
        return file;
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(mActivity, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(mActivity, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }

    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                if (resultCode == mActivity.RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }


    public void onStop() {
        mGoogleApiClient.disconnect(); // Disconnect the client from Google Drive

    }

    public void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}
