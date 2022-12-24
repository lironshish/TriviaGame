package com.example.triviagame.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.triviagame.Adapters.Trivia_Topic_Card_Adapter;
import com.example.triviagame.Objects.Results;
import com.example.triviagame.Objects.Topic;
import com.example.triviagame.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AllTopicsActivity extends AppCompatActivity {

    //Data base
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;


    //Topic adapter
    private ArrayList<Topic> myTopics = new ArrayList();
    private Trivia_Topic_Card_Adapter adapter;
    private RecyclerView topic_recycler_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
        findViews();
        updateUI();

    }

    private void updateUI() {
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
                    Topic topic = new Topic(title, image, webPage);
                    temp.add(topic);

                }
                for (int i = 0; i < temp.size(); i++) {
                    myTopics.add(temp.get(i));
                }
                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initAdapter() {
        adapter = new Trivia_Topic_Card_Adapter(this, myTopics, new Trivia_Topic_Card_Adapter.TopicListener() {

            @Override
            public void clicked(Topic item, int position) {
                Intent intent = new Intent(AllTopicsActivity.this, QuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("TopicName", item.getTitle());
                bundle.putString("WebPage", item.getWebPage());
                intent.putExtra("Bundle", bundle);
                startActivity(intent);
            }
        });
        topic_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        topic_recycler_view.setAdapter(adapter);


    }


    private void findViews() {
        topic_recycler_view = findViewById(R.id.topic_recycler_view);

    }

}