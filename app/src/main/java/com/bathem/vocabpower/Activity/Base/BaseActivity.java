package com.bathem.vocabpower.Activity.Base;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bathem.vocabpower.R;

/**
 * Created by mehtab on 3/27/16.
 */


public class BaseActivity extends AppCompatActivity {

    protected ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void addProgressBarLoader(int viewId) {
        LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spinnerView = inflater.inflate(R.layout.progress_bar, null);
        RelativeLayout layout = (RelativeLayout)findViewById(viewId);
        layout.addView(spinnerView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        spinnerView.setLayoutParams(layoutParams);
    }

    protected void showConfirmationDialogue(String title, String msg, DialogInterface.OnClickListener positiveButton, DialogInterface negativeButton) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (DialogInterface.OnClickListener) positiveButton)
                .setNegativeButton(android.R.string.no, (DialogInterface.OnClickListener) negativeButton).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
