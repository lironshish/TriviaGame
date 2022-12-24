package com.example.triviagame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviagame.Database.MyDataManager;
import com.example.triviagame.Objects.MyUser;
import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {


    private TextView register;
    private EditText inputUsername, inputEmail, inputPassword;
    private MaterialButton register_BTN;
    private MyUser tempUser;

    private final MyDataManager dataManager = MyDataManager.getInstance();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);


        initViews();
        initButtons();
    }

    private void initButtons() {
        register_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = inputUsername.getText().toString();
                Log.d("pttt", "userName: " + userName);
                String email = inputEmail.getText().toString();
                Log.d("pttt", "email: " + email);
                String password = inputPassword.getText().toString();
                Log.d("pttt", "password: " + password);
                String userID = dataManager.getFirebaseAuth().getCurrentUser().getUid();
                Log.d("pttt", "userID: " + userID);
                tempUser = new MyUser(userName, email, password, userID);
                storeUserInDB(tempUser);

            }
        });
    }

    private void storeUserInDB(MyUser tempUser) {

        DatabaseReference myRef = realtimeDB.getReference("Users").child(tempUser.getUID());
        myRef.child("userName").setValue(tempUser.getName());
        myRef.child("email").setValue(tempUser.getEmail());
        myRef.child("password").setValue(tempUser.getPassword());
        startActivity(new Intent(RegisterActivity.this, AllTopicsActivity.class));
        finish();
    }

    private void initViews() {
        register = findViewById(R.id.register);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        register_BTN = findViewById(R.id.register_BTN);
    }


}