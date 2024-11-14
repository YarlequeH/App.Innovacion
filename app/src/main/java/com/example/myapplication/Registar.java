package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registar extends AppCompatActivity implements View.OnClickListener {
    EditText etnombre, etusuario, etpassword, etedad;
    Button btnregistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registar);

        etnombre = (EditText) findViewById(R.id.EditText_Nombre);
        etusuario = (EditText) findViewById(R.id.EditText_Usuario);
        etpassword = (EditText) findViewById(R.id.EditText_Password);
        etedad = (EditText) findViewById(R.id.EditText_Edad);

        btnregistrar = (Button) findViewById(R.id.Btn_Registrar2);

        btnregistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Obtener los valores de los campos de texto
        final String name = etnombre.getText().toString().trim();
        final String username = etusuario.getText().toString().trim();
        final String password = etpassword.getText().toString().trim();
        final String ageText = etedad.getText().toString().trim();

        // Verificar si los campos están vacíos
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || ageText.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Registar.this);
            builder.setMessage("Por favor, completa todos los campos.")
                    .setNegativeButton("Aceptar", null)
                    .create()
                    .show();
            return;
        }

        // Convertir la edad a entero después de verificar que no está vacío
        final int age = Integer.parseInt(ageText);

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        Intent intent = new Intent(Registar.this, MainActivity.class);
                        Registar.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Registar.this);
                        builder.setMessage("Error")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(name, username, age, password, respoListener);
        RequestQueue queue = Volley.newRequestQueue(Registar.this);
        queue.add(registerRequest);
    }
}





