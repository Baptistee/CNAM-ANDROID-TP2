package com.example.googlemaps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Classe qui permet de créer physiquement la base de données SQL Lite sur le
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private String name;
    private Context context;

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, 1);
        this.context = context;
        this.name = name;
    }

    // TODO : Création d'une table. CREATE TABLE waypoint ...
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS waypoint (" +
                " _id TEXT PRIMARY KEY," +
                " _libelle TEXT," +
                " _latitude TEXT," +
                " _longitude TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Toast.makeText(context, "Mise à jour de la BDD version" + oldVersion + " vers "
                + newVersion, Toast.LENGTH_LONG).show();
        db.execSQL("DROP TABLE IF EXISTS waypoint");
        onCreate(db);
    }
}
