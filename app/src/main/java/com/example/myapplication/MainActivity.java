package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
   Button siguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        siguiente=(Button) findViewById(R.id.Siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dos = new Intent(MainActivity.this, Registar.class);
                MainActivity.this.startActivity(dos);
            }
        });
        EditText et_Usuario = (EditText) findViewById(R.id.Text_Usuario);
        EditText et_Pass = (EditText) findViewById(R.id.Text_Pass);
        Button btn_Iniciar = (Button) findViewById(R.id.Btn_Iniciar);

        btn_Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String username = et_Usuario.getText().toString();
              final String password = et_Pass.getText().toString();

              Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                   public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                String name = jsonResponse.getString("name");
                                int  age  = jsonResponse.getInt("age");

                                Intent intent = new Intent(MainActivity.this, Usuario.class);
                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("age", age);
                                intent.putExtra("password", password);

                                MainActivity.this.startActivity(intent);



                            }else{
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
}
}

