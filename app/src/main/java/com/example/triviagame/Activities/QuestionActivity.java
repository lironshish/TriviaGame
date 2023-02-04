package com.example.triviagame.Activities;

import androidx.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.triviagame.Dialogs.TimeOutDialog;
import com.example.triviagame.Finals.Keys;
import com.example.triviagame.Objects.Results;
import com.example.triviagame.Objects.Sound;
import com.example.triviagame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
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

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.BuildConfig;


public class QuestionActivity extends AppCompatActivity {

    //UI
    private CardView card_question, card_A, card_B, card_C, card_D;
    private TextView TXT_question, answer_A, answer_B, answer_C, answer_D, TXT_coins;
    private MaterialButton next_question;
    private LottieAnimationView loading_LOTTIE_animation;
    private ProgressBar progress_bar;
    private ImageView IMG_back, IMG_coins;


    //Bundle
    private Bundle bundle;
    private String webPage;
    private String userName;
    private boolean premium;

    //Timer
    private CountDownTimer countDownTimer;
    private int timerValue = 20;
    private boolean firstQuestion = true;

    //Questions
    private int index = 0;
    private int correctCount = 0;
    private int wrongCount = 0;
    private Results results = new Results();

    //Sound
    private Sound sound;

    private int score = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        if (getIntent().getBundleExtra(Keys.BUNDLE) != null) {
            this.bundle = getIntent().getBundleExtra(Keys.BUNDLE);
            webPage = bundle.getString(Keys.WEB_PAGE);
            userName = bundle.getString(Keys.USER_NAME);
            premium = Boolean.parseBoolean(bundle.getString(Keys.IS_PREMIUM));
        } else {
            this.bundle = new Bundle();
        }

        sound = new Sound();
        initViews();
        initButtons();

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
        initCards();
    }


    private void initButtons() {

        IMG_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel(); //stop the timer
                Intent intent = new Intent(QuestionActivity.this, AllTopicsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Keys.USER_NAME, userName);
                bundle.putString(Keys.IS_PREMIUM, premium + "");
                intent.putExtra(Keys.BUNDLE, bundle);
                startActivity(intent);
                finish();
            }
        });
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
        if (index <= r.getResults().size() - 1) {
            for (int i = 0; i < r.getResults().get(index).getIncorrect_answers().size(); i++)
                answers.add(r.getResults().get(index).getIncorrect_answers().get(i));

            answers.add(r.getResults().get(index).getCorrect_answer());
            Collections.shuffle(answers);
            TXT_question.setText(Html.fromHtml(r.getResults().get(index).getQuestion()));
            answer_A.setText(Html.fromHtml(answers.get(0)));
            answer_B.setText(Html.fromHtml(answers.get(1)));
            answer_C.setText(Html.fromHtml(answers.get(2)));
            answer_D.setText(Html.fromHtml(answers.get(3)));
            next_question.setEnabled(false);

            if (index == r.getResults().size() - 1)
                next_question.setText("Finish");

            if (!firstQuestion) {
                timerValue = 20;
                countDownTimer.cancel();
                countDownTimer.start();
            }
            TXT_coins.setText("" + score);
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
        TXT_coins = findViewById(R.id.TXT_coins);
        IMG_back = findViewById(R.id.IMG_back);
        IMG_coins = findViewById(R.id.IMG_coins);

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
                next_question.setEnabled(true);
                index++;
                wrongCount++;
                if (score >= 10) {
                    score -= 10;
                }
                card_A.setCardBackgroundColor(getResources().getColor(R.color.grey));
                card_B.setCardBackgroundColor(getResources().getColor(R.color.grey));
                card_C.setCardBackgroundColor(getResources().getColor(R.color.grey));
                card_D.setCardBackgroundColor(getResources().getColor(R.color.grey));
                next_question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (index <= 9) {
                            resetColor();
                            setData(results);
                            enableCards();
                        } else if (index == 10) {
                            GameWon();
                        }
                    }
                });
            }
        }.start();
    }

    public void correctAnswer(CardView card) {
        card.setCardBackgroundColor(getResources().getColor(R.color.green));
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                correctCount++;
                score += 10;
                if (index <= 9) {
                    resetColor();
                    setData(results);
                    enableCards();
                } else if (index == 10) {
                    GameWon();
                }
            }
        });
    }

    public void wrongAnswer(CardView card) {
        sound.setMpAndPlay((ContextWrapper) getApplicationContext(), R.raw.incorrect_answer_sound);
        card.setCardBackgroundColor(getResources().getColor(R.color.red));
        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                wrongCount++;
                if (score >= 10) {
                    score -= 10;
                }
                if (index <= 9) {
                    resetColor();
                    setData(results);
                    enableCards();
                } else if (index == 10) {
                    GameWon();
                }
            }
        });


    }

    private void GameWon() {
        Intent intent = new Intent(QuestionActivity.this, WonActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra(Keys.CORRECT, correctCount);
        intent.putExtra(Keys.WRONG, wrongCount);
        bundle.putString(Keys.USER_NAME, userName);
        bundle.putString(Keys.IS_PREMIUM, premium + "");
        intent.putExtra(Keys.BUNDLE, bundle);
        startActivity(intent);
        finish();
    }

    public void enableCards() {
        card_A.setClickable(true);
        card_B.setClickable(true);
        card_C.setClickable(true);
        card_D.setClickable(true);
    }

    public void disableCards() {
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
        countDownTimer.cancel(); //stop the timer
        if (index <= (results.getResults().size() - 1)) {
            next_question.setEnabled(true); //we can click on 'next' bottom
            if (results.getResults().get(index).getCorrect_answer().equals(textView.getText().toString())) {
                sound.setMpAndPlay((ContextWrapper) getApplicationContext(), R.raw.correct_answer_sound);
                correctAnswer(cardView);
            } else {
                wrongAnswer(cardView);
            }
        }
    }

    private void initCards() {
        card_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setEnabled(true);
                disableCards();
                checkIfCorrectAnswer(card_A, answer_A);
            }
        });
        card_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setEnabled(true);
                disableCards();
                checkIfCorrectAnswer(card_B, answer_B);
            }
        });
        card_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setEnabled(true);
                disableCards();
                checkIfCorrectAnswer(card_C, answer_C);
            }
        });

        card_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_question.setEnabled(true);
                disableCards();
                checkIfCorrectAnswer(card_D, answer_D);
            }
        });

    }

}