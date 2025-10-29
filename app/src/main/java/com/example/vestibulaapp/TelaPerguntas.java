package com.example.vestibulaapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TelaPerguntas extends AppCompatActivity {

    TextView perguntaTexto;
    RadioGroup grupoRespostas;
    Button btnConfirmar;

    ArrayList<DataSnapshot> perguntasSnapList = new ArrayList<>();
    ArrayList<String> respostasUsuario = new ArrayList<>();
    ArrayList<Boolean> respostasCorretas = new ArrayList<>();

    int perguntaAtual = 0;
    String materia, topico;

    QuizDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perguntas);

        perguntaTexto = findViewById(R.id.perguntaTexto);
        grupoRespostas = findViewById(R.id.grupoRespostas);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        dbHelper = new QuizDbHelper(this);

        materia = getIntent().getStringExtra("materia");
        topico = getIntent().getStringExtra("topico");

        carregarPerguntas();
        configurarBotaoConfirmar();
    }

    private void carregarPerguntas() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(materia).child(topico).child("perguntas");

        ref.get().addOnSuccessListener(snapshot -> {
            for (DataSnapshot perguntaSnap : snapshot.getChildren()) {
                if (perguntaSnap.hasChild("respostas")) {
                    perguntasSnapList.add(perguntaSnap);
                }
            }

            if (!perguntasSnapList.isEmpty()) {
                btnConfirmar.setText("Confirmar resposta");
                mostrarPergunta();
            } else {
                perguntaTexto.setText("Nenhuma pergunta encontrada.");
                btnConfirmar.setText("Retornar");
                btnConfirmar.setOnClickListener(v -> {
                    Intent intent = new Intent(TelaPerguntas.this, MainActivity.class);
                    startActivity(intent);
                });

            }
        });
        Log.d("DEBUG", "Total de perguntas: " + perguntasSnapList.size());
    }

    private void configurarBotaoConfirmar() {
        // Habilita o botão confirmar ao selecionar uma resposta
        grupoRespostas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnConfirmar.setEnabled(true);
            }
        });

        // Define a ação ao clicar no botão confirmar
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selecionadoId = grupoRespostas.getCheckedRadioButtonId();
                if (selecionadoId == -1) return; // Nenhuma opção selecionada/

                RadioButton rb = findViewById(selecionadoId);
                String resposta = rb.getText().toString();
                boolean correta = (boolean) rb.getTag();

                String pergunta = perguntasSnapList.get(perguntaAtual)
                        .child("pergunta").getValue(String.class);

                dbHelper.salvarResposta(materia, topico, pergunta, resposta, correta);

                respostasUsuario.add(resposta);
                respostasCorretas.add(correta);

                perguntaAtual++;
                if (perguntaAtual < perguntasSnapList.size()) {
                    mostrarPergunta();
                } else {
                    mostrarResultadoFinal();
                }

                // Desativa o botão até nova seleção
                btnConfirmar.setEnabled(false);
            }
        });
    }


    private void mostrarPergunta() {
        grupoRespostas.removeAllViews();

        DataSnapshot snap = perguntasSnapList.get(perguntaAtual);
        String pergunta = snap.child("pergunta").getValue(String.class);
        perguntaTexto.setText(pergunta);

        for (DataSnapshot respostaSnap : snap.child("respostas").getChildren()) {
            String texto = respostaSnap.child("texto").getValue(String.class);
            Boolean valor = respostaSnap.child("valor").getValue(Boolean.class);

            if (texto != null && valor != null) {
                RadioButton rb = new RadioButton(this);
                rb.setText(texto);
                rb.setTextColor(Color.WHITE);
                rb.setTag(valor);
                grupoRespostas.addView(rb);
            }
        }
    }

    private void mostrarResultadoFinal() {
        int score = dbHelper.contarAcertos(materia, topico);
        int total = perguntasSnapList.size();
        Log.d("DEBUG", "Fim do quiz chamado");

        new AlertDialog.Builder(this)
                .setTitle("Fim do Quiz")
                .setMessage("Você acertou " + score + " de " + total + " perguntas.")
                .setCancelable(false)
                .setPositiveButton("Revisar Respostas", (dialog, which) -> {
                    Intent intent = new Intent(this, RevisaoRespostasActivity.class);
                    intent.putExtra("materia", materia);
                    intent.putExtra("topico", topico);
                    intent.putStringArrayListExtra("respostas_usuario", respostasUsuario);
                    intent.putExtra("score", score);

                    // Converter ArrayList<Boolean> para ArrayList<String>
                    ArrayList<String> respostasCorretasStr = new ArrayList<>();
                    for (Boolean b : respostasCorretas) {
                        respostasCorretasStr.add(String.valueOf(b));
                    }
                    intent.putStringArrayListExtra("respostas_corretas", respostasCorretasStr);

                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Finalizar", (dialog, which) -> finish())
                .show();
    }
}
