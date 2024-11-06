package com.example.sbmobile;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailField, passwordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Inicializar firebase

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Obtener referencias a los campos de correo y contraseña
        TextInputLayout emailLayout = findViewById(R.id.CorreoLogin);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLogin);

        emailField = emailLayout.getEditText();
        passwordField = passwordLayout.getEditText();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //para dar estilo a los botones
        underlineButton(R.id.btnOlvideContra);
        underlineButton(R.id.btnNuevoUsuario);
    }

        private void underlineButton(int buttonId) {
            Button button = findViewById(buttonId);
            button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    public void irRegistro(View vista){
        Intent miIntent = new Intent(this,FormRegistro.class);
        startActivity(miIntent);
    }



    public void loginUser(View view) {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el correo y la contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, PrincipalNavigation.class);
                        startActivity(intent);
                    } else {
                        String errorMessage = task.getException().getMessage();
                        String translatedMessage = ErrorTranslator.translate(errorMessage);
                        Toast.makeText(this, "Error en el inicio de sesión: " + translatedMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}