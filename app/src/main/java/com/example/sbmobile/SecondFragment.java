package com.example.sbmobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentMarker;
    private LatLng selectedLatLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button reportButton = view.findViewById(R.id.button_report);
        reportButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                if (selectedLatLng != null) {
                    DenunciaFragment denunciaFragment = DenunciaFragment.newInstance(selectedLatLng.latitude, selectedLatLng.longitude);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, denunciaFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "Seleccione una ubicación antes de reportar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng puentePiedra = new LatLng(-11.865891, -77.056114);
        currentMarker = mMap.addMarker(new MarkerOptions().position(puentePiedra).title("Marker in Puente Piedra"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puentePiedra, 15));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mMap.setOnMapLongClickListener(latLng -> {
            moveMarker(latLng);
            selectedLatLng = latLng; // Guardar la ubicación seleccionada
            Toast.makeText(getContext(), "Ubicación seleccionada: " + latLng.toString(), Toast.LENGTH_SHORT).show();
        });
    }

    private void moveMarker(LatLng newLocation) {
        if (currentMarker != null) {
            currentMarker.setPosition(newLocation);
        } else {
            currentMarker = mMap.addMarker(new MarkerOptions().position(newLocation).title("Nueva ubicación"));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
