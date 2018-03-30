package com.bathem.vocabpower.Interface;

import com.google.android.gms.drive.DriveId;

/**
 * Created by mehtab on 30/03/2018.
 */

public interface IDriveListener {
    public void onFileExist(DriveId id);
    public void onFileDoesNotExist();
}
