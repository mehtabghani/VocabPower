package com.bathem.vocabpower.Manager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bathem.vocabpower.Enum.DriveMode;
import com.bathem.vocabpower.Helper.DataBaseHelper;
import com.bathem.vocabpower.Interface.IFileStatusListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;



/**
 * Created by mehtab on 30/03/2018.
 */

public class GoogleDriveManager2 {

    private static final String TAG = "GoogleDrive";
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private static final String DATABASE_NAME = DataBaseHelper.DATABASE_NAME;
    private static final String GOOGLE_DRIVE_FILE_NAME = "vocab_master.db";
    private static final String GOOGLE_DRIVE_FOLDER_NAME = "Vocab Master";

    private static GoogleDriveManager2 mManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity mActivity;

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    public static GoogleDriveManager2 getInstance() {

        if(mManager == null) {
            mManager = new GoogleDriveManager2();
        }

        return mManager;
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(mActivity, signInOptions);
    }

    public void initGoogleClient(Activity activity, DriveMode mode, IFileStatusListener iFileStatusListener) {
        mActivity = activity;
        mGoogleSignInClient = buildGoogleSignInClient();

        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
        task.addOnSuccessListener(
                new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        Log.i(TAG, "Google service sign in success");
                        // Build a drive client.
                        mDriveClient = Drive.getDriveClient(mActivity, googleSignInAccount);
                        // Build a drive resource client.
                        mDriveResourceClient =
                                Drive.getDriveResourceClient(mActivity, googleSignInAccount);

                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Sign in failed", e);
                            }
                        });

    }



 }
