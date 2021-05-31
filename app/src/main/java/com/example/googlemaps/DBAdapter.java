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
    private SQLiteDatabase db;
    private String name;
    private static DBAdapter INSTANCE = null;

    private DBAdapter(Context context, String name) {
        this.context = context;
        this.name = name;
        dbHelper = new DatabaseHelper(this.context, this.name);
    }

    public static DBAdapter getInstance(Context context, String name) {

        if (INSTANCE == null) {
            INSTANCE = new DBAdapter(context, name);
        }
        return INSTANCE;
    }

    public static DBAdapter getInstance() {
        return INSTANCE;
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

    public long insertWaypoint(String pId, String pLibelle, String pLatitude, String pLongitude) {

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
    public void removeWaypoint(String pId) {

        String[] id = {pId};

        this.db.delete("waypoint", "id", id);
    }

    public Cursor retrieveWaypointList() {
        // utiliser la méthode db.query pour retourner cette liste.

        return this.db.query("waypoint", null, null, null, null, null, null);
    }

    public boolean deleteWaypoint(String pId) {

        db.execSQL("DELETE FROM waypoint WHERE _id = '" + pId + "'");

        return true;
    }

    public String getWaypointNameByID(int pId) {

        Cursor c = this.db.query("waypoint", new String[] { "_libelle" }, "_id = ?", new String[] { String.valueOf(pId) }, null, null, null);
        c.moveToFirst();

        return c.getString(0);
    }

    public float getWaypointLatByID (int pId) {

        Cursor c = this.db.query("waypoint", new String[] { "_latitude" }, "_id = ?", new String[] { String.valueOf(pId) }, null, null, null);
        c.moveToFirst();

         return c.getFloat(0);
    }

    public float getWaypointLonByID (int pId) {

        Cursor c = this.db.query("waypoint", new String[] { "_longitude" }, "_id = ?", new String[] { String.valueOf(pId) }, null, null, null);
        c.moveToFirst();

        return c.getFloat(0);
    }
}
