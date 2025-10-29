package com.example.vestibulaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    Button btnGeografia, btnHistoria, btnSociologia, btnFilosofia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Vincula o botão
        btnGeografia = findViewById(R.id.btnGeografia);
        btnFilosofia = findViewById(R.id.btnFilosofia);
        btnHistoria = findViewById(R.id.btnHistoria);
        btnSociologia = findViewById(R.id.btnSociologia);

        // Define ação ao clicar
        btnGeografia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, telaTopicosMateria.class);
            intent.putExtra("referencia", "Geografia");
            startActivity(intent);
        });

        btnSociologia.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, telaTopicosMateria.class);
            intent.putExtra("referencia", "Sociologia");
            startActivity(intent);
        });

        btnHistoria.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, telaTopicosMateria.class);
            intent.putExtra("referencia", "Historia");
            startActivity(intent);

        });

        btnFilosofia.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, telaTopicosMateria.class);
            intent.putExtra("referencia", "Filosofia");
            startActivity(intent);

        });
    }
}
