package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText etUsuario, etPass;
    private Button btnIniciar;
    private Switch switchRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperar preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (isLoggedIn) {
            // Redirigir según el estado almacenado del Switch
            if (rememberMe) {
                navigateToInterfas();
            } else {
                // Limpiar sesión si no se debe recordar
                sharedPreferences.edit().clear().apply();
                setContentView(R.layout.activity_main);
            }
        } else {
            setContentView(R.layout.activity_main);
        }

        // Configuración de vistas
        initializeViews();
    }

    private void initializeViews() {
        etUsuario = findViewById(R.id.Text_Usuario);
        etPass = findViewById(R.id.Text_Pass);
        btnIniciar = findViewById(R.id.Btn_Iniciar);
        switchRemember = findViewById(R.id.switch_remember);
        TextView tvSiguiente = findViewById(R.id.Siguiente);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etUsuario.getText().toString();
                final String password = etPass.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String name = jsonResponse.getString("name");
                                int age = jsonResponse.getInt("age");

                                // Guardar sesión y estado del switch
                                SharedPreferences.Editor editor = getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putBoolean("rememberMe", switchRemember.isChecked());
                                editor.apply();

                                // Redirigir según el estado del Switch
                                Intent intent;
                                if (switchRemember.isChecked()) {
                                    intent = new Intent(MainActivity.this, Interfaz.class);
                                } else {
                                    intent = new Intent(MainActivity.this, Usuario.class);
                                }

                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                intent.putExtra("age", age);
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Login Fallido")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });

        tvSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registar.class);
                startActivity(intent);
            }
        });
    }

    // Métodos para redirigir a las actividades
    private void navigateToUsuario() {
        Intent intent = new Intent(MainActivity.this, Usuario.class);
        startActivity(intent);
        finish();
    }

    private void navigateToInterfas() {
        Intent intent = new Intent(MainActivity.this, Interfaz.class);
        startActivity(intent);
        finish();
    }
}




