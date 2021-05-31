package com.example.googlemaps;

import android.content.Intent;
import android.os.Bundle;

import android.app.ListActivity;
import android.database.Cursor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class WaypointListActivity extends ListActivity {

    private DBAdapter dbAdapter;
    private String dbName;

    private static final int MENU_ITEM_SUIVRE = 101;
    private static final int MENU_ITEM_SUPPRIMER = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint_list);

        dbName = "madb";

        // "accéder à la ListView, lui brancher un menu contextuel permettant de supprimer un
        // élément de la liste, on rajoutera les rubriques permettant d'éditer un élément
        // (sans implémenter le code).

        // Créer l'adapteur pour la BDD.
        dbAdapter = DBAdapter.getInstance(this, dbName);

        // Ouvrir la BDD.
        dbAdapter.open();

        // Charger les données de la BDD vers l'IHM.
        dataBindWaypoint();

        registerForContextMenu(getListView());
    }

    @Override
    protected void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Options element");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_SUIVRE , 0, "Suivre");
        menu.add(0, MENU_ITEM_SUPPRIMER , 1, "Supprimer");

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case MENU_ITEM_SUIVRE :
                Intent intent1 = new Intent(this, MapsActivity.class);
                intent1.putExtra("waypointID", String.valueOf(info.id));
                startActivity(intent1);
                break;

            case MENU_ITEM_SUPPRIMER :
                Intent intent2 = new Intent(this, WaypointListActivity.class);
                supprimer(info);
                startActivity(intent2);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void dataBindWaypoint() {

        setContentView(R.layout.activity_waypoint_list);
        Cursor cursor = dbAdapter.retrieveWaypointList();
        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.user_item,
                cursor,
                new String[]{"_id", "_libelle", "_latitude", "_longitude"},
                new int[]{R.id.id, R.id.libelle, R.id.latitude, R.id.longitude}
        );

        setListAdapter(adapter);
    }

    public void ajouterWaypoint(View view) {
        EditText id = (EditText) findViewById(R.id.idText);
        EditText libelle = (EditText) findViewById(R.id.libelleText);
        EditText latitude = (EditText) findViewById(R.id.latituideText);
        EditText longitude = (EditText) findViewById(R.id.longitudeText);

        this.dbAdapter.insertWaypoint(id.getText().toString(),
                libelle.getText().toString(),
                latitude.getText().toString(),
                longitude.getText().toString());

        dataBindWaypoint();
    }

    public void supprimer(AdapterView.AdapterContextMenuInfo info) {
        this.dbAdapter.deleteWaypoint(String.valueOf(info.id));
    }
}