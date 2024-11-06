package com.example.sbmobile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topicList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Topic topic, String topicId);
        void onViewPostClick(Topic topic, String topicId);
    }

    public TopicAdapter(List<Topic> topicList, OnItemClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);
        String topicId = topic.getTopicId();  // Necesitarás guardar el ID del tema de alguna manera

        String title = topic.getTitle() != null ? topic.getTitle() : "";
        String description = topic.getDescription() != null ? topic.getDescription() : "";

        holder.titleTextView.setText(title);
        holder.descriptionTextView.setText(description);

        // Configurar MapView (si es necesario)
        if (holder.topicMapView != null) {
            holder.topicMapView.onCreate(null);
            holder.topicMapView.onResume();
            holder.topicMapView.getMapAsync(googleMap -> {
                // Agregar un marcador con la ubicación del topic
                if (topic.getLatitude() != 0 && topic.getLongitude() != 0) {
                    LatLng location = new LatLng(topic.getLatitude(), topic.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(location).title("Ubicación de la publicación"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
            });
        }

        // Configurar onClick para el botón de ver comentarios
        holder.viewCommentsButton.setOnClickListener(v -> listener.onItemClick(topic, topicId));

        // Configurar onClick para el nuevo botón de ver el post completo
        holder.viewPostButton.setOnClickListener(v -> listener.onViewPostClick(topic, topicId));
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        MapView topicMapView;
        Button viewCommentsButton;
        Button viewPostButton;  // Nuevo botón para ver el post

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.topicTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.topicDescriptionTextView);
            topicMapView = itemView.findViewById(R.id.topicMapView);
            viewCommentsButton = itemView.findViewById(R.id.viewCommentsButton);
            viewPostButton = itemView.findViewById(R.id.viewPostButton); // Inicializar el nuevo botón
        }
    }
}
