package com.example.googlemaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Classe permettant d'accéder à la données d'un point de vue écriture lecture mise à jour et
 * suppression de données (CRUD).
 */

public class DBAdapter {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db; // Notre BDD.
    private String name; // Nom de la BDD.

    public DBAdapter(Context context, String name) {
        this.context = context;
        this.name = name;
        dbHelper = new DatabaseHelper(this.context, this.name);
    }

    public DBAdapter open() {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void deleteAll() {
        db.execSQL("DROP TABLE IF EXISTS waypoint");
    }

    // Les paramètres sont les champs d'un element à insérer dans la table.
    public long insertItem(String pId, String pLibelle, String pLatitude, String pLongitude) {

        // Créer la requête d'insertion d'un élément dans la table.

        ContentValues values = new ContentValues();
        values.put("_id", pId);
        values.put("_libelle", pLibelle);
        values.put("_latitude", pLatitude);
        values.put("_longitude", pLongitude);

        return this.db.insert("waypoint", null, values);
    }

    // l'id est la clé primaire de l'élément à supprimer de la table.
    // gérer le retour en boolean et plus en void.
    public void removeItem(String pId) {

        String[] id = {pId};

        this.db.delete("waypoint", "id", id);
    }

    // Retourne la liste complète des éléments de la table.
    public Cursor retrieveItemList() {
        // utiliser la méthode db.query pour retourner cette liste.

        return this.db.query("waypoint", null, null, null, null, null, null);
    }
}
