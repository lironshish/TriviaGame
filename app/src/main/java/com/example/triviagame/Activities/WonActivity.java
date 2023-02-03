package com.example.triviagame.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.triviagame.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class WonActivity extends AppCompatActivity {

    //UI
    private CircularProgressBar circularProgressBar;
    private TextView result_text;
    private LottieAnimationView win_LOTTIE_animation;
    private ImageView IMG_back;

    //Counters
    private int correct, wrong;

    private String userName;
    private Bundle bundle;
    private boolean premium;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);


        if (getIntent().getBundleExtra("Bundle") != null) {
            this.bundle = getIntent().getBundleExtra("Bundle");
            userName = bundle.getString("userName");
            premium = Boolean.parseBoolean(bundle.getString("isPremium"));
        } else {
            this.bundle = new Bundle();
        }

        correct = getIntent().getIntExtra("correct", 0);
        wrong = getIntent().getIntExtra("wrong", 0);

        initViews();
        initButtons();

        circularProgressBar.setProgress(correct);
        circularProgressBar.setProgressMax(correct + wrong);
        result_text.setText(correct + "/" + (correct + wrong));
    }

    private void initButtons() {
        IMG_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WonActivity.this, AllTopicsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", userName);
                bundle.putString("isPremium", premium + "");
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        circularProgressBar = findViewById(R.id.circularProgressBar);
        result_text = findViewById(R.id.result_text);
        win_LOTTIE_animation = findViewById(R.id.win_LOTTIE_animation);
        IMG_back = findViewById(R.id.IMG_back);

    }
}