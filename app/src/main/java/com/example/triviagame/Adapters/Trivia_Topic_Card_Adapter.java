package com.example.triviagame.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.triviagame.Objects.Topic;
import com.example.triviagame.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Trivia_Topic_Card_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface TopicListener {
        void clicked(Topic item, int position, boolean premium);
    }


    private Activity activity;
    private ArrayList<Topic> topics = new ArrayList<>();
    private TopicListener topicListener;
    private boolean premium;

    public Trivia_Topic_Card_Adapter(Activity activity, boolean premium, ArrayList<Topic> topics, TopicListener topicListener) {
        this.activity = activity;
        this.topics = topics;
        this.topicListener = topicListener;
        this.premium = premium;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trivia_topic_card, parent, false);
        TopicHolder topicHolder = new TopicHolder(view);
        return topicHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final TopicHolder holder = (TopicHolder) viewHolder;
        Topic topic = getItem(position);
        holder.topic_name.setText(topic.getTitle());

        Glide
                .with(activity)
                .load(topic.getImage())
                .into(holder.topic_image);

        Log.d("pttt", "holder "+ premium);


        holder.disablePremiumCards(position, premium);

    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public Topic getItem(int position) {
        return topics.get(position);
    }


    class TopicHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView topic_image;
        private AppCompatImageView disable_card;
        private MaterialTextView topic_name;
        private LinearLayoutCompat premium_LAY_locked;


        public TopicHolder(View itemView) {
            super(itemView);
            topic_image = itemView.findViewById(R.id.topic_image);
            topic_name = itemView.findViewById(R.id.topic_name);
            disable_card = itemView.findViewById(R.id.disable_card);
            premium_LAY_locked = itemView.findViewById(R.id.premium_LAY_locked);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (topicListener != null) {
                        topicListener.clicked(getItem(getAdapterPosition()), getAdapterPosition(), premium);
                    }
                }
            });


        }

        public void disablePremiumCards(int position, boolean premium) {
            if (!premium && position < 4) {
                premium_LAY_locked.setVisibility(View.INVISIBLE);
                disable_card.setVisibility(View.INVISIBLE);
            } else if (premium){
                premium_LAY_locked.setVisibility(View.INVISIBLE);
                disable_card.setVisibility(View.INVISIBLE);
            }

        }

    }
}
