package com.example.vestibulaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "quiz.db";
    private static final int DB_VERSION = 2;

    public QuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE respostas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "materia TEXT, " +
                "topico TEXT, " +
                "pergunta TEXT, " +
                "resposta_usuario TEXT, " +
                "correta INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS respostas");
        onCreate(db);
    }

    public void salvarResposta(String materia, String topico, String pergunta, String respostaUsuario, boolean correta) {
        if (materia == null || topico == null || pergunta == null || respostaUsuario == null) return;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("materia", materia);
        valores.put("topico", topico);
        valores.put("pergunta", pergunta);
        valores.put("resposta_usuario", respostaUsuario);
        valores.put("correta", correta ? 1 : 0);
        db.insert("respostas", null, valores);
    }

    public int contarRespostas(String materia, String topico) {
        if (materia == null || topico == null) return 0;

        SQLiteDatabase db = getReadableDatabase();
        int total = 0;

        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM respostas WHERE materia = ? AND topico = ?",
                new String[]{materia, topico})) {
            if (cursor.moveToFirst()) {
                total = cursor.getInt(0);
            }
        }

        return total;
    }

    public int contarAcertos(String materia, String topico) {
        if (materia == null || topico == null) return 0;

        SQLiteDatabase db = getReadableDatabase();
        int acertos = 0;

        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM respostas WHERE materia = ? AND topico = ? AND correta = 1",
                new String[]{materia, topico})) {
            if (cursor.moveToFirst()) {
                acertos = cursor.getInt(0);
            }
        }

        return acertos;
    }

    public ArrayList<String> listarRespostasRevisao(String materia, String topico) {
        ArrayList<String> respostas = new ArrayList<>();
        if (materia == null || topico == null) return respostas;

        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.rawQuery(
                "SELECT pergunta, resposta_usuario FROM respostas WHERE materia = ? AND topico = ?",
                new String[]{materia, topico})) {

            while (cursor.moveToNext()) {
                String pergunta = cursor.getString(cursor.getColumnIndexOrThrow("pergunta"));
                String resposta = cursor.getString(cursor.getColumnIndexOrThrow("resposta_usuario"));
                respostas.add("Pergunta: " + pergunta + "\nSua resposta: " + resposta);
            }
        }

        return respostas;
    }
}
