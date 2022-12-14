package com.example.triviagame.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.triviagame.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class WonActivity extends AppCompatActivity {

    //UI
    private CircularProgressBar circularProgressBar;
    private TextView result_text;
    private LottieAnimationView win_LOTTIE_animation;

    //Counters
    int correct, wrong;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        correct = getIntent().getIntExtra("correct",0);
        wrong = getIntent().getIntExtra("wrong",0);
        initViews();


        circularProgressBar.setProgress(correct);
        circularProgressBar.setProgressMax(correct+wrong);
        result_text.setText(correct+"/"+(correct+wrong));
    }

    private void initViews() {
        circularProgressBar = findViewById(R.id.circularProgressBar);
        result_text = findViewById(R.id.result_text);
        win_LOTTIE_animation = findViewById(R.id.win_LOTTIE_animation);
    }
}