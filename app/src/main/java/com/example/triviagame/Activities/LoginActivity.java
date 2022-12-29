package com.example.triviagame.Activities;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.triviagame.Database.MyDataManager;
import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private TextView login, textViewSignUp;
    private EditText inputEmail, inputPassword;
    private MaterialButton login_BTN, privacy_policy_BTN, terms_of_use_BTN, about_BTN;

    private final MyDataManager dataManager = MyDataManager.getInstance();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();

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

        login_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputEmail.getText().toString().isEmpty() || inputPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                } else if (!inputEmail.getText().toString().isEmpty() && !inputPassword.getText().toString().isEmpty()) {
                    login(inputEmail.getText().toString(), inputPassword.getText().toString());
                }
            }
        });

    }

    private void initViews() {
        login = findViewById(R.id.login);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        login_BTN = findViewById(R.id.login_BTN);
        privacy_policy_BTN = findViewById(R.id.privacy_policy_BTN);
        terms_of_use_BTN = findViewById(R.id.terms_of_use_BTN);
        about_BTN = findViewById(R.id.about_BTN);
    }


    private void login(String email, String password) {
        DatabaseReference myRef = dataManager.getRealTimeDB().getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue().toString().equals(email)) {
                        if (dataSnapshot.child("password").getValue().equals(password)) {
                            Intent intent = new Intent(LoginActivity.this, AllTopicsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", dataSnapshot.child("userName").getValue().toString());
                            intent.putExtra("Bundle", bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "The password is incorrect, please try again", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "The email is not registered in the system", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


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