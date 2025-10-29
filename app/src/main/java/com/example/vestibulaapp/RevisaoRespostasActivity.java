package com.example.vestibulaapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RevisaoRespostasActivity extends AppCompatActivity {

    private ListView listViewRevisao;
    private ArrayList<String> respostasRevisao = new ArrayList<>();
    private ArrayList<String> respostasCorretasStr = new ArrayList<>();
    private ArrayList<Boolean> respostasCorretas = new ArrayList<>();
    private ArrayList<String> respostasUsuario = new ArrayList<>();
    private String materia, topico;
    private TextView txtScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisao_respostas);

        listViewRevisao = findViewById(R.id.listaRevisao);
        txtScore = findViewById(R.id.txtScore);

        materia = getIntent().getStringExtra("materia");
        topico = getIntent().getStringExtra("topico");
        respostasUsuario = getIntent().getStringArrayListExtra("respostas_usuario");
        respostasCorretasStr = getIntent().getStringArrayListExtra("respostas_corretas");
        int score = getIntent().getIntExtra("score", 0);


        for (String s : respostasCorretasStr) {
            respostasCorretas.add(Boolean.parseBoolean(s));
        }

        txtScore.setText("Você acertou: " + score + " de " + respostasUsuario.size());
        carregarRespostas();
    }

    private void carregarRespostas() {
        for (int i = 0; i < respostasUsuario.size(); i++) {
            String resposta = respostasUsuario.get(i);
            boolean correta = respostasCorretas.get(i);
            String status = correta ? "Resposta Correta" : "Resposta Incorreta";
            respostasRevisao.add("Pergunta " + (i + 1) + ": " + resposta + "\n" + status);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, respostasRevisao);
        listViewRevisao.setAdapter(adapter);
    }
}
