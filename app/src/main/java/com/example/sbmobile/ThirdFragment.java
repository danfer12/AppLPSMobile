package com.example.sbmobile;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ThirdFragment extends Fragment {

    private RecyclerView topicRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference topicsRef;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        topicRecyclerView = view.findViewById(R.id.topicRecyclerView);
        database = FirebaseDatabase.getInstance();
        topicsRef = database.getReference("topics");

        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(topicList, new TopicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Topic topic, String topicId) {
                // Redirigir a CommentsActivity pasando el ID del tópico
                Intent intent = new Intent(getContext(), CommentsActivity.class);
                intent.putExtra("TOPIC_ID", topicId);
                startActivity(intent);
            }

            @Override
            public void onViewPostClick(Topic topic, String topicId) {
                // Redirigir a PostDetailActivity para ver los detalles del post
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                intent.putExtra("TOPIC_ID", topicId);
                intent.putExtra("TITLE", topic.getTitle());
                intent.putExtra("DESCRIPTION", topic.getDescription());
                intent.putExtra("IMAGE_URL", topic.getImageUrl());
                intent.putExtra("TIMESTAMP", topic.getTimestamp());
                intent.putExtra("USERNAME", topic.getUsername());
                intent.putExtra("LATITUDE", topic.getLatitude());
                intent.putExtra("LONGITUDE", topic.getLongitude());
                startActivity(intent);
            }
        });

        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topicRecyclerView.setAdapter(topicAdapter);

        loadTopics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTopics(); // Recargar los tópicos al regresar al fragmento
    }

    private void loadTopics() {
        topicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Topic topic = postSnapshot.getValue(Topic.class);
                    if (topic != null) {
                        topic.setTopicId(postSnapshot.getKey());
                        topicList.add(topic);
                    }
                }
                topicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de carga de datos
            }
        });
    }
}
