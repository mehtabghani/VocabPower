package com.bathem.vocabpower.Interface;

import com.google.android.gms.drive.DriveFile;

/**
 * Created by mehtab on 30/03/2018.
 */

public interface IDriveListener {
    public void onFileExist(DriveFile driveFile);
    public void onFileDoesNotExist();
}
