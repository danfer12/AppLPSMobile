package com.example.sbmobile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, timestampTextView, userNameTextView;
    private ImageView postImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Inicializar vistas
        titleTextView = findViewById(R.id.postTitleTextView);
        descriptionTextView = findViewById(R.id.postDescriptionTextView);
        timestampTextView = findViewById(R.id.postTimestampTextView);
        userNameTextView = findViewById(R.id.postUserNameTextView);
        postImageView = findViewById(R.id.postImageView);

        // Obtener los extras del intent
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String timestamp = getIntent().getStringExtra("TIMESTAMP");
        String userName = getIntent().getStringExtra("USERNAME");

        // Configurar los datos en las vistas
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        timestampTextView.setText(timestamp);
        userNameTextView.setText(userName);

        // Cargar la imagen con Glide
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(postImageView);
        } else {
            postImageView.setImageResource(R.drawable.placeholder_image); // Imagen de marcador de posición
        }
    }
}
