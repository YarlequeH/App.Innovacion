package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LibraryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
        // Constructor vacío requerido
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);


        Button btnEstructura = view.findViewById(R.id.btn_estructura);
        btnEstructura.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), EstructuraActivity.class);
            startActivity(intent);
        });

        Button btnViolencia = view.findViewById(R.id.btn_Violencia_C_M_F);
        btnViolencia.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ViolenciaActivity.class);
            startActivity(intent);
        });
        Button btnRobo = view.findViewById(R.id.btn_Robo_deD);
        btnRobo.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), RoboActivity.class);
            startActivity(intent);
        });
        Button btnAcoso = view.findViewById(R.id.btn_Acoso);
        btnAcoso.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AcosoAtcivity.class);
            startActivity(intent);
        });
        Button btnAgresion = view.findViewById(R.id.Por_Agresion);
        btnAgresion.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AgresionActivity.class);
            startActivity(intent);
        });
        Button btnMaltrato = view.findViewById(R.id.btn_maltrato_animal);
        btnMaltrato.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MaltratoActivity.class);
            startActivity(intent);
        });



        return view;
    }
}












