package com.example.triviagame.Activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.triviagame.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
    }


    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets) {
        String str = "";
        InputStream is = null;

        try {
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setPositiveButton("Close", null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            materialAlertDialogBuilder.setMessage(Html.fromHtml(str));
        }

        AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }


}