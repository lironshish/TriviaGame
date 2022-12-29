package com.example.triviagame.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.triviagame.Dialogs.TimeOutDialog;
import com.example.triviagame.Objects.Results;
import com.example.triviagame.Objects.Sound;
import com.example.triviagame.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class QuestionActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private CardView card_question, card_A, card_B, card_C, card_D;
    private TextView TXT_question, answer_A, answer_B, answer_C, answer_D;
    private MaterialButton next_question;
    private LottieAnimationView loading_LOTTIE_animation;


    //Bundle
    private Bundle bundle;

    private String currentTopic;
    private String webPage;

    private CountDownTimer countDownTimer;
    private int timerValue = 20;
    private ProgressBar progress_bar;
    private boolean firstQuestion = true;

    int index = 0;
    int correctCount = 0;
    int wrongCount = 0;
    Results results = new Results();

    private Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (getIntent().getBundleExtra("Bundle") != null) {
            this.bundle = getIntent().getBundleExtra("Bundle");
            currentTopic = bundle.getString("TopicName");
            webPage = bundle.getString("WebPage");
        } else {
            this.bundle = new Bundle();
        }

        sound = new Sound();
        initViews();

        makeServerCall(new UpdateUI() {
            @Override
            public void update(Results r) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        setData(r);
                    }
                });

            }
        });

        next_question.setClickable(false);
        cardAClick();
        cardBClick();
        cardCClick();
        cardDClick();
    }


    private void loading() {

        loading_LOTTIE_animation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                loading_LOTTIE_animation.setVisibility(View.VISIBLE);
                card_question.setVisibility(View.INVISIBLE);
                card_A.setVisibility(View.INVISIBLE);
                card_B.setVisibility(View.INVISIBLE);
                card_C.setVisibility(View.INVISIBLE);
                card_D.setVisibility(View.INVISIBLE);
                TXT_question.setVisibility(View.INVISIBLE);
                answer_A.setVisibility(View.INVISIBLE);
                answer_B.setVisibility(View.INVISIBLE);
                answer_C.setVisibility(View.INVISIBLE);
                answer_D.setVisibility(View.INVISIBLE);
                next_question.setVisibility(View.INVISIBLE);
                progress_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                loading_LOTTIE_animation.setVisibility(View.INVISIBLE);
                card_question.setVisibility(View.VISIBLE);
                card_A.setVisibility(View.VISIBLE);
                card_B.setVisibility(View.VISIBLE);
                card_C.setVisibility(View.VISIBLE);
                card_D.setVisibility(View.VISIBLE);
                TXT_question.setVisibility(View.VISIBLE);
                answer_A.setVisibility(View.VISIBLE);
                answer_B.setVisibility(View.VISIBLE);
                answer_C.setVisibility(View.VISIBLE);
                answer_D.setVisibility(View.VISIBLE);
                next_question.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.VISIBLE);

                initTimer();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void setData(Results r) {
        ArrayList<String> answers = new ArrayList<>();
        if (index < r.getResults().size() - 1) {
            for (int i = 0; i < r.getResults().get(index).getIncorrect_answers().size(); i++)
                answers.add(r.getResults().get(index).getIncorrect_answers().get(i));

            answers.add(r.getResults().get(index).getCorrect_answer());
            Collections.shuffle(answers);
            TXT_question.setText(Html.fromHtml(r.getResults().get(index).getQuestion()));
            answer_A.setText(Html.fromHtml(answers.get(0)));
            answer_B.setText(Html.fromHtml(answers.get(1)));
            answer_C.setText(Html.fromHtml(answers.get(2)));
            answer_D.setText(Html.fromHtml(answers.get(3)));
            if (!firstQuestion) {
                timerValue = 20;
                countDownTimer.cancel();
                countDownTimer.start();
            }
            firstQuestion = false;

        }
    }


    interface UpdateUI {
        public void update(Results r);
    }

    public void makeServerCall(UpdateUI updateUI) {
        new Thread() {
            public void run() {

                loading();
                try (InputStream is = new URL(webPage).openStream();
                     Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

                    Gson gson = new Gson();
                    results = gson.fromJson(reader, Results.class);
                    updateUI.update(results);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    private void initViews() {
        // toolbar = findViewById(R.id.toolbar);
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

        progress_bar = findViewById(R.id.progress_bar);
        next_question = findViewById(R.id.next_question);

        loading_LOTTIE_animation = findViewById(R.id.loading_LOTTIE_animation);
    }

    private void initTimer() {
        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                timerValue -= 1;
                progress_bar.setProgress(timerValue);
            }

            @Override
            public void onFinish() {
                TimeOutDialog timeOutDialog = new TimeOutDialog();
                timeOutDialog.show(QuestionActivity.this);
            }
        }.start();
    }

    public void correctAnswer(CardView card) {

        card.setCardBackgroundColor(getResources().getColor(R.color.green));
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctCount++;
                Log.d("pttt", "correctCount " + correctCount);
                index++;
                resetColor();
                setData(results);
                enableButtons();

            }
        });

    }

    public void wrongAnswer(CardView card) {
        sound.setMpAndPlay((ContextWrapper) getApplicationContext(), R.raw.incorrect_answer_sound);
        card.setCardBackgroundColor(getResources().getColor(R.color.red));
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wrongCount++;
                Log.d("pttt", "wrongCount " + wrongCount);
                if (index < results.getResults().size()) {
                    index++;
                    resetColor();
                    setData(results);
                    enableButtons();
                } else {
                    GameWon();
                }
            }
        });


    }

    private void GameWon() {
        Intent intent = new Intent(QuestionActivity.this, WonActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        startActivity(intent);
    }

    public void enableButtons() {
        card_A.setClickable(true);
        card_B.setClickable(true);
        card_C.setClickable(true);
        card_D.setClickable(true);
    }

    public void disableButtons() {
        card_A.setClickable(false);
        card_B.setClickable(false);
        card_C.setClickable(false);
        card_D.setClickable(false);
    }

    public void resetColor() {
        card_A.setCardBackgroundColor(getResources().getColor(R.color.white));
        card_B.setCardBackgroundColor(getResources().getColor(R.color.white));
        card_C.setCardBackgroundColor(getResources().getColor(R.color.white));
        card_D.setCardBackgroundColor(getResources().getColor(R.color.white));
    }


    private void checkIfCorrectAnswer(CardView cardView, TextView textView) {
        countDownTimer.cancel();
        if (index < results.getResults().size() - 1) {
            next_question.setClickable(true);
            disableButtons();
            if (results.getResults().get(index).getCorrect_answer().equals(textView.getText().toString())) {
                sound.setMpAndPlay((ContextWrapper) getApplicationContext(), R.raw.correct_answer_sound);
                cardView.setCardBackgroundColor(getResources().getColor(R.color.green));
                if (index < results.getResults().size() - 1) {
                    correctAnswer(cardView);
                } else {
                    GameWon();
                }
            } else {
                wrongAnswer(cardView);
            }
        }
    }


    public void cardAClick() {

        card_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setClickable(true);
                disableButtons();
                checkIfCorrectAnswer(card_A, answer_A);


            }
        });
    }


    public void cardBClick() {
        card_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setClickable(true);
                disableButtons();
                checkIfCorrectAnswer(card_B, answer_B);
            }
        });
    }


    public void cardCClick() {
        card_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setClickable(true);
                disableButtons();
                checkIfCorrectAnswer(card_C, answer_C);
            }
        });


    }

    public void cardDClick() {
        card_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setClickable(true);
                disableButtons();
                checkIfCorrectAnswer(card_D, answer_D);
            }
        });

    }

}