package com.example.triviagame.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private TextView login, textViewSignUp;
    private EditText inputEmail, inputPassword;
    private MaterialButton login_BTN, privacy_policy_BTN, terms_of_use_BTN, about_BTN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
        initButtons();
    }

    private void initButtons() {
        privacy_policy_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHtmlTextDialog(LoginActivity.this, "privacy_policy.html");
            }
        });


        terms_of_use_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHtmlTextDialog(LoginActivity.this, "terms_of_use.html");
            }
        });


        about_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHtmlTextDialog(LoginActivity.this, "about.html");
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initViews() {
        login = findViewById(R.id.login);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        inputEmail = findViewById(R.id.inputEmail);
        login_BTN = findViewById(R.id.login_BTN);
        privacy_policy_BTN = findViewById(R.id.privacy_policy_BTN);
        terms_of_use_BTN = findViewById(R.id.terms_of_use_BTN);
        about_BTN = findViewById(R.id.about_BTN);
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