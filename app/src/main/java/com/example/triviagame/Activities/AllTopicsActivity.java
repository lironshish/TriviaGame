package com.example.triviagame.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviagame.Adapters.Trivia_Topic_Card_Adapter;
import com.example.triviagame.Finals.Keys;
import com.example.triviagame.Objects.Topic;
import com.example.triviagame.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllTopicsActivity extends AppCompatActivity {

    //Data base
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    //Topic adapter
    private ArrayList<Topic> myTopics = new ArrayList();
    private Trivia_Topic_Card_Adapter adapter;
    private RecyclerView topic_recycler_view;
    private TextView title;

    private ImageView IMG_logout;

    //Bundle
    private Bundle bundle;
    private String userName;
    private boolean premium;

    //Banner
    private FrameLayout main_LAY_banner;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    //Ads
   // private RewardedAd mRewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
      //  loadVideoAd();
        if (getIntent().getBundleExtra(Keys.BUNDLE) != null) {
            this.bundle = getIntent().getBundleExtra(Keys.BUNDLE);
            userName = bundle.getString(Keys.USER_NAME);
            premium = Boolean.parseBoolean(bundle.getString(Keys.IS_PREMIUM));
        } else {
            this.bundle = new Bundle();
        }

        findViews();
        initButtons();
        title.setText("Hi " + userName + "," + "\n" + "Please choose questions topic");
        title.setGravity(Gravity.CENTER);
        updateUI(premium);
        initAnalytics();

    }

    private void initButtons() {
        IMG_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllTopicsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void updateUI(boolean premium) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Topic> temp = new ArrayList<>();
                for (DataSnapshot child : snapshot.child("Topics").getChildren()) {
                    String image = child.child("Image").getValue().toString();
                    String title = child.child("Title").getValue().toString();
                    String webPage = child.child("WebPage").getValue().toString();
                    Topic topic = new Topic(title, image, webPage, premium);
                    temp.add(topic);

                }
                for (int i = 0; i < temp.size(); i++) {
                    myTopics.add(temp.get(i));
                }
                initAdapter(premium);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(!premium){
            Log.d("pttt", "Liron ");
            showBanner();
        }
    }


    private void initAdapter(boolean premium) {
        adapter = new Trivia_Topic_Card_Adapter(this, premium, myTopics, new Trivia_Topic_Card_Adapter.TopicListener() {

            @Override
            public void clicked(Topic item, int position, boolean premium) {
                if ((!premium && position < 4) || (premium)) {
                    Intent intent = new Intent(AllTopicsActivity.this, QuestionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.WEB_PAGE, item.getWebPage());
                    bundle.putString(Keys.USER_NAME, userName);
                    bundle.putString(Keys.IS_PREMIUM, premium + "");
                    intent.putExtra(Keys.BUNDLE, bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AllTopicsActivity.this, "You need Premium Subscription", Toast.LENGTH_SHORT).show();
                }

            }
        });
        topic_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        topic_recycler_view.setAdapter(adapter);
    }


    private void findViews() {
        main_LAY_banner = findViewById(R.id.main_LAY_banner);
        topic_recycler_view = findViewById(R.id.topic_recycler_view);
        title = findViewById(R.id.title);
        IMG_logout = findViewById(R.id.IMG_logout);

//        action_a = findViewById(R.id.action_a);
//        action_b = findViewById(R.id.action_b);

    }

//    private void actionA() {
//        showBanner();
//    }
//
//    private void actionB() {
//        showVideoAd();
//    }


    private void initAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setUserId("test0");
    }

    private void showBanner() {
        String UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
        AdView adView = new AdView(this);
        adView.setAdUnitId(UNIT_ID);
        main_LAY_banner.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

//    private void loadVideoAd() {
//        // action_a.setEnabled(false);
//        String UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
//        if (BuildConfig.DEBUG) {
//            UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
//        }
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        RewardedAd.load(this, UNIT_ID,
//                adRequest, new RewardedAdLoadCallback() {
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error.
//                        Log.d("pttt", loadAdError.toString());
//                        mRewardedAd = null;
//                    }
//
//                    @Override
//                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
//                        mRewardedAd = rewardedAd;
//                        //action_b.setEnabled(true);
//                        Log.d("pttt", "Ad was loaded.");
//                    }
//                });
//    }
//
//
//    private void showVideoAd() {
//        mRewardedAd.show(this, new OnUserEarnedRewardListener() {
//            @Override
//            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
//                Toast.makeText(AllTopicsActivity.this, "Congr... +1 Live", Toast.LENGTH_SHORT).show();
//                loadVideoAd();
//            }
//        });
//    }

}