package com.bathem.vocabpower.Manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Interface.IDriveListener;
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
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private static final String GOOGLE_DRIVE_FILE_NAME = DATABASE_NAME+"_1.0.db";
    private static final String GOOGLE_DRIVE_FOLDER_NAME = "Vocab Master";

    private static GoogleDriveManager2 mManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity mActivity;
    private DataBaseHelper mDBHelper;
    private Context mAppContext;
    private DriveMode mDriveMode;



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
        mDriveMode = mode;
        signIn();

        // Log.d(TAG, ""+mAppContext.getDatabasePath(DATABASE_NAME));
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


        if(mDriveMode == DriveMode.backup){
            startBackUp();
        } else if(mDriveMode == DriveMode.restore) {
            searchFile(new IDriveListener() {
                @Override
                public void onFileExist(DriveFile driveFile) {
                    openFile(driveFile);
                }
                @Override
                public void onFileDoesNotExist() {

                }
            });
        }
    }


//Backup methods
    private void startBackUp()  {

        searchFile(new IDriveListener() {
            @Override
            public void onFileExist(DriveFile driveFile) {
                deleteFile(driveFile);
            }

            @Override
            public void onFileDoesNotExist() {
                //upload db
                createFileOnGDrive();
            }
        });

    }

    private void deleteFile(DriveFile file) {

        mDriveResourceClient
                .delete(file)
                .addOnSuccessListener(mActivity,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, GOOGLE_DRIVE_FILE_NAME + "has been removed from GDrive");

                                //upload db
                                createFileOnGDrive();
                            }
                        })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to delete file", e);

                    }
                });
    }

    private void createFileOnGDrive() {

        final File file = getDbPath();

        if(!file.exists()) {
            Log.v(TAG, "DB file does not exist.");
            return;
        }

        final String mimeType = "application/x-sqlite3";//MimeTypeMap.getSingleton().getMimeTypeFromExtension(".db");


        final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getRootFolder();
        final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(new Continuation<Void, Task<DriveFile>>() {
                    @Override
                    public Task<DriveFile> then(@NonNull Task<Void> task) throws Exception {
                        DriveFolder parent = rootFolderTask.getResult();
                        DriveContents contents = createContentsTask.getResult();

                        FileInputStream is = new FileInputStream(file);
                        BufferedInputStream in = new BufferedInputStream(is);
                        byte[] buffer = new byte[8 * 1024];
                        BufferedOutputStream out = new BufferedOutputStream(contents.getOutputStream());
                        int n = 0;
                        while( ( n = in.read(buffer) ) > 0 ) {
                            out.write(buffer, 0, n);
                        }

                        in.close();

                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(GOOGLE_DRIVE_FILE_NAME)
                                .setMimeType(mimeType)
                                .setStarred(true)
                                .build();

                        return mDriveResourceClient.createFile(parent, changeSet, contents);
                    }
                })
                .addOnSuccessListener(mActivity,
                        new OnSuccessListener<DriveFile>() {
                            @Override
                            public void onSuccess(DriveFile driveFile) {
                                Log.d(TAG, GOOGLE_DRIVE_FILE_NAME+" uploaded on GDrive");
                                mFileStatusListener.onFileUploaded();
                            }
                        })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to create file", e);
                        mFileStatusListener.onFileFailed(DriveMode.backup);

                    }
                });

    }


// Restore Methods


    private void searchFile(final IDriveListener iDriveListener) {
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
                                    iDriveListener.onFileExist(mDriveFile);
                                } else {
                                    iDriveListener.onFileDoesNotExist();
                                }

                            }
                        })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
                        restoreDB(contents);


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


   void restoreDB(DriveContents contents) {
       InputStream inputStream = contents.getInputStream();
       mDBHelper.restoreDB(inputStream, mAppContext, mFileStatusListener);
   }

    private File getDbPath() {
        File file = mActivity.getDatabasePath(DATABASE_NAME);
        Log.d(TAG, "DB path: " + file.getAbsolutePath());
        return file;
    }

}

