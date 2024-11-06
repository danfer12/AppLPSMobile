package com.example.sbmobile;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DenunciaFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private double latitude;
    private double longitude;

    private EditText topicTitleInput;
    private EditText commentInput;
    private Button postCommentButton, selectImageButton;
    private RecyclerView commentRecyclerView;
    private ImageView selectedImageView;
    private Uri imageUri;
    private TextView userNameTextView;

    private FirebaseDatabase database;
    private DatabaseReference topicsRef;
    private StorageReference storageRef;

    private FirebaseAuth mAuth;

    public static DenunciaFragment newInstance(double latitude, double longitude) {
        DenunciaFragment fragment = new DenunciaFragment();
        Bundle args = new Bundle();
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_denuncia, container, false);

        if (getArguments() != null) {
            latitude = getArguments().getDouble("latitude");
            longitude = getArguments().getDouble("longitude");
        }

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        topicsRef = database.getReference("topics");
        storageRef = FirebaseStorage.getInstance().getReference("topic_images");

        // Inicialización de vistas
        topicTitleInput = view.findViewById(R.id.topicTitleInput);
        commentInput = view.findViewById(R.id.commentInput);
        postCommentButton = view.findViewById(R.id.postCommentButton);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        selectedImageView = view.findViewById(R.id.selectedImageView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        commentRecyclerView = view.findViewById(R.id.commentRecyclerView);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Obtener y mostrar el nombre del usuario
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = database.getReference("users").child(userId);

            userRef.get().addOnSuccessListener(dataSnapshot -> {
                String userName = dataSnapshot.child("name").getValue(String.class);
                if (userName != null) {
                    userNameTextView.setText("Usuario: " + userName);
                } else {
                    userNameTextView.setText("Usuario: Anónimo");
                }
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show());
        }

        postCommentButton.setOnClickListener(v -> postComment());
        selectImageButton.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
        }
    }

    private void postComment() {
        final String title = topicTitleInput.getText() != null ? topicTitleInput.getText().toString() : "";
        final String description = commentInput.getText() != null ? commentInput.getText().toString() : "";

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "El título y la descripción no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = database.getReference("users").child(userId);

            userRef.get().addOnSuccessListener(dataSnapshot -> {
                String userName = dataSnapshot.child("name").getValue(String.class);
                if (userName == null) {
                    userName = "Usuario Anónimo";
                }

                String timestamp = DateFormat.format("dd/MM/yyyy HH:mm:ss", Calendar.getInstance(Locale.getDefault())).toString();

                if (imageUri != null) {
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                    String finalUserName = userName;
                    fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        String topicId = topicsRef.push().getKey();
                        Map<String, Object> newTopic = new HashMap<>();
                        newTopic.put("title", title);
                        newTopic.put("description", description);
                        newTopic.put("imageUrl", imageUrl);
                        newTopic.put("userName", finalUserName);
                        newTopic.put("timestamp", timestamp);
                        newTopic.put("latitude", latitude);
                        newTopic.put("longitude", longitude);

                        if (topicId != null) {
                            topicsRef.child(topicId).setValue(newTopic).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Nuevo tema añadido con éxito", Toast.LENGTH_SHORT).show();
                                    topicTitleInput.setText("");
                                    commentInput.setText("");
                                    selectedImageView.setImageResource(0);
                                    imageUri = null;
                                } else {
                                    Toast.makeText(getContext(), "Error al añadir el tema", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })).addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    String topicId = topicsRef.push().getKey();
                    Map<String, Object> newTopic = new HashMap<>();
                    newTopic.put("title", title);
                    newTopic.put("description", description);
                    newTopic.put("imageUrl", "");
                    newTopic.put("userName", userName);
                    newTopic.put("timestamp", timestamp);
                    newTopic.put("latitude", latitude);
                    newTopic.put("longitude", longitude);

                    if (topicId != null) {
                        topicsRef.child(topicId).setValue(newTopic).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Nuevo tema añadido con éxito", Toast.LENGTH_SHORT).show();
                                topicTitleInput.setText("");
                                commentInput.setText("");
                            } else {
                                Toast.makeText(getContext(), "Error al añadir el tema", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show());
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
