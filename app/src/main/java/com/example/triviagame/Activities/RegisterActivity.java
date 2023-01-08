package com.example.triviagame.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviagame.Database.MyDataManager;
import com.example.triviagame.Objects.MyUser;
import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


public class RegisterActivity extends AppCompatActivity {

    //UI
    private TextView register;
    private EditText inputUsername, inputEmail, inputPassword;
    private MaterialButton register_BTN;
    private CheckBox premium_account;
    //Local user
    private MyUser tempUser;
    //DB
    private final MyDataManager dataManager = MyDataManager.getInstance();
    private final FirebaseDatabase realtimeDB = dataManager.getRealTimeDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initButtons();
    }

    private void initButtons() {
        register_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = inputUsername.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                boolean premium = premium_account.isChecked();
                if (emailValidator(email)) {
                    tempUser = new MyUser(userName, email, password, premium);
                    storeUserInDB(tempUser);
                }
            }
        });
    }


    public boolean emailValidator(String email) {
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            Toast.makeText(this, "Enter valid Email address please", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void storeUserInDB(MyUser tempUser) {
        UUID uuid = UUID.randomUUID();
        tempUser.setUID(uuid.toString());
        DatabaseReference myRef = realtimeDB.getReference("Users").child(uuid.toString());
        myRef.child("userName").setValue(tempUser.getName());
        myRef.child("email").setValue(tempUser.getEmail());
        myRef.child("password").setValue(tempUser.getPassword());
        myRef.child("premium").setValue(tempUser.isPremium());
        Intent intent = new Intent(RegisterActivity.this, AllTopicsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", tempUser.getName());
        if(tempUser.isPremium())
            bundle.putString("isPremium", "true");
        else
            bundle.putString("isPremium", "false");
        intent.putExtra("Bundle", bundle);
        startActivity(intent);
        finish();
    }

    private void initViews() {
        register = findViewById(R.id.register);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        register_BTN = findViewById(R.id.register_BTN);
        premium_account = findViewById(R.id.premium_account);
    }


}