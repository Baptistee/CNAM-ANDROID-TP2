package com.example.googlemaps;

import android.os.Bundle;

import android.app.ListActivity;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.Menu;

public class WaypointListActivity extends ListActivity {

    private DBAdapter dbAdapter;
    private String dbName;

    private static final int MENU_ITEM_EDITER = 101;
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
        dbAdapter = new DBAdapter(this, dbName);

        // Ouvrir la BDD.
        dbAdapter.open();

        // Charger les données de la BDD vers l'IHM.
        dataBindWaypoint();

        registerForContextMenu(getListView());
    }

    // mettre en place ce qu'il faut pour avoir un menu d'options permettant de tout effacer
    // en BDD.

    // ne pas oublier de surcharger la méthode onDestroy qui doit "faire écho"
    // à la méthode onCreate.
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
        menu.add(0, MENU_ITEM_EDITER , 0, "Editer [bouton demo]");
        menu.add(0, MENU_ITEM_SUPPRIMER , 1, "Supprimer [bouton demo]");

        super.onCreateContextMenu(menu, view, menuInfo);
    }

    // charger l'IHM avec le contenu de la base.
    public void dataBindWaypoint() {

        setContentView(R.layout.activity_waypoint_list);
        Cursor cursor = dbAdapter.retrieveItemList();
        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.user_item,
                cursor,
                new String[]{"_id", "_libelle", "_latitude", "_longitude"},
                new int[]{R.id.id, R.id.libelle, R.id.latitude, R.id.longitude}
        );

        setListAdapter(adapter);
    }

    // Modifier l'IHM pour qu'elle comporte un bouton permettant de rajouter un élément en
    // base de données (puis de rafraichir l'IHM).
    // Un id unique doit être créé à chaque fois (par exemple basé sur le temps).
    // cf. SystemClock.currentThreadTimeInMillis.

    // C'est la fonction execute part le bouton ajouter waypoint, j'ai modifié son onClick dans
    // le XML.
    public void ajouterWaypoint(View view)
    {
        EditText id = (EditText) findViewById(R.id.idText);
        EditText libelle = (EditText) findViewById(R.id.libelleText);
        EditText latitude = (EditText) findViewById(R.id.latituideText);
        EditText longitude = (EditText) findViewById(R.id.longitudeText);

        this.dbAdapter.insertItem(id.getText().toString(),
                libelle.getText().toString(),
                latitude.getText().toString(),
                longitude.getText().toString());

        dataBindWaypoint();
    }
}