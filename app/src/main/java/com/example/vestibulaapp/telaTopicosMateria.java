package com.example.vestibulaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class telaTopicosMateria extends AppCompatActivity {

    ListView listaTopicos;
    ArrayAdapter<TopicoInfo> adapter;
    ArrayList<TopicoInfo> topicosInfo = new ArrayList<>();
    TextView txtViewMateria;
    QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_topicos_materia);

        listaTopicos = findViewById(R.id.listaTopicos);
        txtViewMateria = findViewById(R.id.txtViewMateria);
        dbHelper = new QuizDbHelper(this);

        // Obtendo referência da matéria passada
        String referencia = getIntent().getStringExtra("referencia");
        DatabaseReference materiaRef = FirebaseDatabase.getInstance().getReference(referencia);

        // Exibindo o nome da matéria
        txtViewMateria.setText(referencia);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topicosInfo);
        listaTopicos.setAdapter(adapter);

        // Recuperando tópicos do Firebase
        materiaRef.get().addOnSuccessListener(snapshot -> {
            topicosInfo.clear();  // Limpar lista de tópicos antes de adicionar os novos dados

            for (DataSnapshot topicoSnap : snapshot.getChildren()) {
                String nomeTopico = topicoSnap.getKey();
                // Adicionar cada tópico à lista
                TopicoInfo info = new TopicoInfo(nomeTopico, 0, 0);  // Definindo valores fictícios para acertos e total
                topicosInfo.add(info);
            }

            // Atualizar a lista de tópicos na interface
            adapter.notifyDataSetChanged();
        });

        // Ação ao clicar em um tópico
        listaTopicos.setOnItemClickListener((parent, view, position, id) -> {
            TopicoInfo topico = topicosInfo.get(position);

            // Ir diretamente para a tela de perguntas
            Intent intent = new Intent(this, TelaPerguntas.class);
            intent.putExtra("materia", referencia);  // Passando a referência da matéria
            intent.putExtra("topico", topico.nome);  // Passando o nome do tópico
            startActivity(intent);
        });
    }
}
