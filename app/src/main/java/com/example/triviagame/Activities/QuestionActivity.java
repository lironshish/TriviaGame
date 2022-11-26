package com.example.triviagame.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.triviagame.R;

public class QuestionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView card_question, card_A, card_B, card_C, card_D;
    private TextView TXT_question, answer_A, answer_B, answer_C, answer_D;


    //Bundle
    private Bundle bundle;

    private String currentTopic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        if (getIntent().getBundleExtra("Bundle") != null) {
            this.bundle = getIntent().getBundleExtra("Bundle");
            currentTopic = bundle.getString("TopicName");
        } else {
            this.bundle = new Bundle();
        }

        Log.d("pttt","topic name " + currentTopic);
        initViews();

    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        card_question = findViewById(R.id.card_question);
        card_A = findViewById(R.id.card_A);
        card_B = findViewById(R.id.card_B);
        card_C = findViewById(R.id.card_C);
        card_D = findViewById(R.id.card_D);
        TXT_question = findViewById(R.id.TXT_question);
        answer_A = findViewById(R.id.answer_A);
        answer_B = findViewById(R.id.answer_B);
        answer_C = findViewById(R.id.answer_C);
        answer_D = findViewById(R.id.answer_D);

    }


    private void checkIfTheAnswerIsCorrect(CardView card, String str){
      //  if(str.)
    }


    private void changeQuestion() {

    }






}