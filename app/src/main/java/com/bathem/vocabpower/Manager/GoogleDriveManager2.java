package com.bathem.vocabpower.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Interface.IFileStatusListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;


/**
 * Created by mehtab on 30/03/2018.
 */

public class GoogleDriveManager2 {

    /**
     * Request code for Google Sign-in
     */
    protected static final int REQUEST_CODE_SIGN_IN = 0;
    /**
     * Request code for the Drive picker
     */
    protected static final int REQUEST_CODE_OPEN_ITEM = 1;

    private static final String TAG = "GoogleDrive";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String DATABASE_NAME = DataBaseHelper.DATABASE_NAME;
    private static final String GOOGLE_DRIVE_FILE_NAME = DATABASE_NAME;
    private static final String GOOGLE_DRIVE_FOLDER_NAME = "Vocab Master";

    private static GoogleDriveManager2 mManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity mActivity;
    private DataBaseHelper mDBHelper;
    private Context mAppContext;



    //Google Drive instances
    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;
    private DriveFile mDriveFile;


    /**
     * Tracks completion of the drive picker
     */
    private TaskCompletionSource<DriveId> mOpenItemTaskSource;

    IFileStatusListener mFileStatusListener;

    public static GoogleDriveManager2 getInstance() {

        if(mManager == null) {
            mManager = new GoogleDriveManager2();
        }

        return mManager;
    }

    public void initGoogleClient(Activity activity, DriveMode mode, IFileStatusListener iFileStatusListener) {
        mActivity = activity;
        mFileStatusListener = iFileStatusListener;
        mAppContext =    mActivity.getApplication();
        mDBHelper = new DataBaseHelper(mActivity);
        //signIn();

         Log.d(TAG, ""+mAppContext.getDatabasePath(DATABASE_NAME));
    }

    /**
     * Starts the sign-in process and initializes the Drive client.
     */
    protected void signIn() {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        //requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(mActivity);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            initializeDriveClient(signInAccount);

            Log.d(TAG, "Already Sign-in");

        } else {
            GoogleSignInOptions signInOptions =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestScopes(Drive.SCOPE_FILE)
                            //.requestScopes(Drive.SCOPE_APPFOLDER)
                            .build();
            mGoogleSignInClient = GoogleSignIn.getClient(mActivity, signInOptions);
            mActivity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK) {

                    Task<GoogleSignInAccount> getAccountTask =
                            GoogleSignIn.getSignedInAccountFromIntent(data);
                    if (getAccountTask.isSuccessful()) {
                        initializeDriveClient(getAccountTask.getResult());
                        Log.d(TAG, "Sign-in sucessfull.");


                    } else {
                        Log.d(TAG, "Sign-in failed.");
                        mActivity.finish();
                    }

                }
                break;
            case REQUEST_CODE_OPEN_ITEM:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    //mOpenItemTaskSource.setResult(driveId);

                    Log.d(TAG, "File opened");

                } else {
                    mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }

                break;
        }
    }

    /**
     * Continues the sign-in process, initializing the Drive clients with the current
     * user's account.
     */
    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        mDriveClient = Drive.getDriveClient(mActivity, signInAccount);
        mDriveResourceClient = Drive.getDriveResourceClient(mActivity, signInAccount);
        searchFile();
    }


    private void searchFile() {
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                .build();
        Task<MetadataBuffer> queryTask = mDriveResourceClient.query(query);
        queryTask
                .addOnSuccessListener(mActivity,
                        new OnSuccessListener<MetadataBuffer>() {
                            @Override
                            public void onSuccess(MetadataBuffer metadataBuffer) {
                                // Handle results...
                                Log.d(TAG, GOOGLE_DRIVE_FILE_NAME + " found");

                                if(metadataBuffer.getCount()>0) {
                                    mDriveFile = metadataBuffer.get(0).getDriveId().asDriveFile();
                                    openFile(mDriveFile);
                                }

                            }
                        })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure...
                        // ...

                        Log.e(TAG, "Error retrieving files", e);
                        mFileStatusListener.onFileFailed(DriveMode.restore);

                    }
                });

    }



    private void openFile(DriveFile file) {
        Task<DriveContents> openFileTask =
                mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);

        openFileTask
                .continueWithTask(new Continuation<DriveContents, Task<Void>>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {


                        DriveContents contents = task.getResult();

                        Log.d(TAG, "File opened successfully and ready to restore");

                        InputStream inputStream = contents.getInputStream();
                        mDBHelper.restoreDB(inputStream, mAppContext, mFileStatusListener);

                        Task<Void> discardTask = mDriveResourceClient.discardContents(contents);
                        return discardTask;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to open file.", e);

                    }
                });
    }


    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @return Task that resolves with the selected item's ID.
     */
    protected Task<DriveId> pickFolder() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(
                                Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                        .build();
        return pickItem(openOptions);
    }


    /**
     * Prompts the user to select a text file using OpenFileActivity.
     *
     * @return Task that resolves with the selected item's ID.
     */
    protected Task<DriveId> pickTextFile() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(Filters.eq(SearchableField.TITLE, GOOGLE_DRIVE_FILE_NAME))
                        //.setActivityTitle(mActivity.getString(R.string.select_file))
                        .build();
        return pickItem(openOptions);
    }

    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @param openOptions Filter that should be applied to the selection
     * @return Task that resolves with the selected item's ID.
     */
    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        mDriveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith(new Continuation<IntentSender, Object>() {
                    @Override
                    public Object then(@NonNull Task<IntentSender> task) throws Exception {
                        mActivity.startIntentSenderForResult(
                                task.getResult(), REQUEST_CODE_OPEN_ITEM, null, 0, 0, 0);
                        return null;
                    }
                });

        return mOpenItemTaskSource.getTask();
    }


    private File getDbPath() {
        File file = mActivity.getDatabasePath(DATABASE_NAME);
        Log.d(TAG, "DB path: " + file.getAbsolutePath());
        return file;
    }

}
