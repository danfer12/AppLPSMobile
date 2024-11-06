package com.example.sbmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final String TAG = "FirstFragment";

    private String selectedPhoneNumber;

    public FirstFragment() {
        // Constructor vacío requerido
    }

    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargarDatosDistritos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Configurar el Spinner para seleccionar el distrito
        Spinner districtSpinner = view.findViewById(R.id.districtSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtPhoneMap.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = parent.getItemAtPosition(position).toString();
                selectedPhoneNumber = districtPhoneMap.get(selectedDistrict);
                Toast.makeText(getContext(), "Teléfono seleccionado: " + selectedPhoneNumber, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPhoneNumber = null;
            }
        });

        // Configurar el OnClickListener para el botón de emergencias principales
        Button buttonAlert = view.findViewById(R.id.EmergenciasPrincipales);
        buttonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAlerta();
            }
        });

        // Configurar el OnClickListener para el botón de llamada al serenazgo
        Button buttonSerenazgo = view.findViewById(R.id.serenazgoEmergencyButton);
        buttonSerenazgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPhoneNumber != null) {
                    makePhoneCall(selectedPhoneNumber);
                } else {
                    Toast.makeText(getContext(), "Seleccione un distrito primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el OnClickListener para el botón de enviar alerta/mensaje de ayuda
        Button buttonSendAlert = view.findViewById(R.id.sendAlertButton);
        buttonSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar la lógica para enviar un mensaje de alerta aquí
            }
        });

        return view;
    }

    public void irAlerta() {
        Intent miIntent = new Intent(getActivity(), Alerta.class);
        startActivity(miIntent);
    }
    private Map<String, String> districtPhoneMap = new HashMap<>();
    private void cargarDatosDistritos() {
        districtPhoneMap.put("Ate Vitarte", "014941210");
        districtPhoneMap.put("Barranco", "012470366");
        districtPhoneMap.put("Bellavista", "014535109");
        districtPhoneMap.put("Breña", "014239492");
        districtPhoneMap.put("Carmen de la Legua", "014643747");
        districtPhoneMap.put("Cercado Callao", "014299520");
        districtPhoneMap.put("Chaclacayo", "013582415");
        districtPhoneMap.put("Chorrillos", "012517001");
        districtPhoneMap.put("Jesús María", "014611070");
        districtPhoneMap.put("La Victoria", "014313000");
        districtPhoneMap.put("La Molina", "013134455");
        districtPhoneMap.put("La Perla", "014205604");
        districtPhoneMap.put("La Punta", "014534979");
        districtPhoneMap.put("Lima Cercado", "013185050");
        districtPhoneMap.put("Lince", "014716389, 012658783");
        districtPhoneMap.put("Los Olivos", "016138210, 016138270, 016138280, 016138290");
        districtPhoneMap.put("Miraflores", "016177281, 016177578, 016177532");
        districtPhoneMap.put("Pueblo Libre", "013193160");
        districtPhoneMap.put("Rímac", "015004040, 015004044");
        districtPhoneMap.put("Santa Anita", "013630396");
        districtPhoneMap.put("San Borja", "016311000");
        districtPhoneMap.put("San Isidro", "013190450");
        districtPhoneMap.put("San Martín de Porres", "017146000");
        districtPhoneMap.put("San Miguel", "013133003");
        districtPhoneMap.put("Santiago de Surco", "014115555");
        districtPhoneMap.put("Surquillo", "014481680");
        // Agrega otros distritos si es necesario
    }


    private void makePhoneCall(String phoneNumber) {
        Log.d(TAG, "Intentando realizar llamada a " + phoneNumber);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
    }
}
