package com.example.triviagame.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviagame.Adapters.Trivia_Topic_Card_Adapter;
import com.example.triviagame.Objects.Topic;
import com.example.triviagame.R;
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

    //Bundle
    private Bundle bundle;
    private String userName;
    private boolean premium;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);

        if (getIntent().getBundleExtra("Bundle") != null) {
            this.bundle = getIntent().getBundleExtra("Bundle");
            userName = bundle.getString("userName");
            premium = Boolean.parseBoolean(bundle.getString("isPremium"));
        } else {
            this.bundle = new Bundle();
        }


        findViews();
        title.setText("Hi " + userName + "," + "\n" + "Please choose questions topic");

        updateUI(premium);

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
    }


    private void initAdapter(boolean premium) {
        adapter = new Trivia_Topic_Card_Adapter(this, premium, myTopics, new Trivia_Topic_Card_Adapter.TopicListener() {

            @Override
            public void clicked(Topic item, int position, boolean premium) {
                if ((!premium && position < 4) || (premium)) {
                    Intent intent = new Intent(AllTopicsActivity.this, QuestionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("WebPage", item.getWebPage());
                    intent.putExtra("Bundle", bundle);
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
        topic_recycler_view = findViewById(R.id.topic_recycler_view);
        title = findViewById(R.id.title);
    }

}