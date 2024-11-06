package com.example.sbmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FormRegistro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText nameField, emailField, passwordField, phoneField, confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);
        phoneField = findViewById(R.id.phone);
    }

    public void registerUser(View view) {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String phone = phoneField.getText().toString().trim();

        // Validar nombre
        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar correo electrónico
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "El formato del correo electrónico es inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar número de teléfono
        if (phone.length() != 9) {
            Toast.makeText(this, "El número de teléfono debe tener 9 dígitos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar contraseñas
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Las contraseñas no pueden estar vacías.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario con Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                            // Crear un mapa con los datos del usuario
                            Map<String, String> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);

                            // Guardar los datos del usuario en la base de datos
                            usersRef.child(userId).setValue(userData).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, PrincipalNavigation.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Error al guardar los datos del usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void irPrincipal(View vista) {
        Intent miIntent = new Intent(this, PrincipalNavigation.class);
        startActivity(miIntent);
    }
}
