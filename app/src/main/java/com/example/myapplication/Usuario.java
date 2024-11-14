package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Usuario extends AppCompatActivity {
TextView tv_Nombre, tv_Usuario, tv_Edad ,tv_Contraseña ;
Button btn_Inter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        tv_Nombre = (TextView) findViewById(R.id.Text_Nombre);
        tv_Usuario = (TextView) findViewById(R.id.Text_Usuario);
        tv_Edad = (TextView) findViewById(R.id.Text_Edad);
        tv_Contraseña = (TextView) findViewById(R.id.Text_Password);

        Intent integer = getIntent();
        String name = integer.getStringExtra("name");
        String username = integer.getStringExtra("username");
        String password = integer.getStringExtra("password");
        int age = integer.getIntExtra("age", -1);

        tv_Nombre.setText(name);
        tv_Usuario.setText(username);
        tv_Contraseña.setText(password);
        tv_Edad.setText(age+"");

        btn_Inter = (Button) findViewById(R.id.Btn_Inicio);
        btn_Inter.setOnClickListener(view -> {
            Intent intent = new Intent(Usuario.this, Interfaz.class);
            startActivity(intent);
        });
    }
}