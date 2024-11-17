package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // Parámetros utilizados en otro lugar del fragmento
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Variables de parámetros
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Constructor vacío
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Encuentra los botones por su ID
        Button btnAmbulance = rootView.findViewById(R.id.btnAmbulance);
        Button btnFire = rootView.findViewById(R.id.btnFire);
        Button btnPolice = rootView.findViewById(R.id.btnPolice);

        // Configura los listeners de los botones
        btnAmbulance.setOnClickListener(v -> dialEmergencyNumber("116"));
        btnFire.setOnClickListener(v -> dialEmergencyNumber("116"));
        btnPolice.setOnClickListener(v -> dialEmergencyNumber("105"));

        return rootView;
    }

    // Método para marcar el número de emergencia
    private void dialEmergencyNumber(String number) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }
}

