package com.bathem.vocabpower.Interface;

import com.bathem.vocabpower.Enum.DriveMode;

/**
 * Created by mehtab on 09/06/16.
 */
public interface IFileStatusListener {

    public void onFileRestored();
    public void onFileUploaded();
    public void onFileFailed(DriveMode mode);
}
