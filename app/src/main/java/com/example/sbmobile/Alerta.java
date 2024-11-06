package com.example.sbmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Alerta extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final String TAG = "AlertaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alerta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Verificar y solicitar permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }

        // Configurar los botones para realizar llamadas
        configurarBotonLlamada(R.id.buttonEsSalud, "tel:014118000"); // EsSalud para violencia
        configurarBotonLlamada(R.id.buttonViolenciaFamiliar, "tel:100"); // Denuncia contra la violencia
        configurarBotonLlamada(R.id.buttonCentralPolicial, "tel:105"); // Central policial
        configurarBotonLlamada(R.id.buttonCovidInfo, "tel:107"); // EsSalud info COVID-19
        configurarBotonLlamada(R.id.buttonPoliciaCarreteras, "tel:110"); // Policía de carreteras
        configurarBotonLlamada(R.id.buttonInfosalud, "tel:113"); // Infosalud
        configurarBotonLlamada(R.id.buttonDefensaCivil, "tel:115"); // Defensa Civil
        configurarBotonLlamada(R.id.buttonBomberos, "tel:116"); // Bomberos
        configurarBotonLlamada(R.id.buttonCruzRoja, "tel:012660481"); // Cruz Roja
        configurarBotonLlamada(R.id.buttonSamu, "tel:106"); // SAMU
        configurarBotonLlamada(R.id.buttonAlertaMedica, "tel:014166777"); // Alerta Médica
        configurarBotonLlamada(R.id.buttonClaveMedica, "tel:012658783"); // Clave Médica
        configurarBotonLlamada(R.id.buttonAmbulanciasEsSalud, "tel:117"); // Ambulancias EsSalud
    }

    private void configurarBotonLlamada(int buttonId, String phoneNumber) {
        ImageButton button = findViewById(buttonId); // Cambiado a ImageButton
        if (button != null) {
            button.setOnClickListener(v -> {
                Log.d(TAG, "Botón presionado para llamada a " + phoneNumber);
                makePhoneCall(phoneNumber);
            });
        } else {
            Log.e(TAG, "No se encontró el botón con ID: " + buttonId);
        }
    }

    private void makePhoneCall(String phoneNumber) {
        Log.d(TAG, "Intentando realizar llamada a " + phoneNumber);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(phoneNumber));
            try {
                startActivity(callIntent);
            } catch (Exception e) {
                Log.e(TAG, "Error al intentar realizar la llamada", e);
            }
        } else {
            Log.d(TAG, "Permiso CALL_PHONE no concedido. Solicitando permiso.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permiso CALL_PHONE concedido.");
            } else {
                Log.d(TAG, "Permiso CALL_PHONE denegado.");
            }
        }
    }
}
