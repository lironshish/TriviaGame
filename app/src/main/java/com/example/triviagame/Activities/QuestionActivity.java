package com.example.triviagame.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.triviagame.R;

public class QuestionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView card_question, card_answer_A, card_answer_B, card_answer_C, card_answer_D;
    private TextView text_question, text_answer_A, text_answer_B, text_answer_C, text_answer_D;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initViews();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

    }
}