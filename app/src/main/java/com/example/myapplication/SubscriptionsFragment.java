package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

public class SubscriptionsFragment extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;

    public SubscriptionsFragment() {
        // Constructor vacío necesario
    }

    public static SubscriptionsFragment newInstance(String param1, String param2) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar el MapView
        mapView = view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();

            try {
                MapsInitializer.initialize(requireActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap map) {
                    googleMap = map;

                    // Configuración adicional del mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}



