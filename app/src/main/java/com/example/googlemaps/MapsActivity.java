package com.example.googlemaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;

    private DBAdapter dbAdapter;
    private String dbName;

    private final static int GROUP_ID = 0;
    private final static int ORDER = 0;
    private final static int ITEMZOOMIN_ID = 100;
    private final static int ITEMZOOMOUT_ID = 101;
    private final static int ITEMSAT_ID = 200;
    private final static int ITEMTRAFIC_ID = 201;
    private final static int ITEMSTREET_ID = 202;
    private final static int ITEMWAYPOINTLIST_ID = 203;

    private final static double LATITUDE = 48.875536;
    private final static double LONGITUDE = 2.335796;

    boolean modeTrafic = true;

    private String waypointID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dbName = "madb";

        // Créer l'adapteur pour la BDD.
        dbAdapter = DBAdapter.getInstance();

        // Ouvrir la BDD.
        //dbAdapter.open();

        Intent intentReceived = getIntent();
        waypointID = intentReceived.getStringExtra("waypointID");

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Start GPS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        }
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, this);
        onLocationChanged(this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(GROUP_ID, ITEMZOOMIN_ID, ORDER, "Zoom in");
        menu.add(GROUP_ID, ITEMZOOMOUT_ID, ORDER, "Zoom out");
        menu.add(GROUP_ID, ITEMSAT_ID, ORDER, "Vue satellite");
        menu.add(GROUP_ID, ITEMTRAFIC_ID, ORDER, "Vue trafic");
        menu.add(GROUP_ID, ITEMSTREET_ID, ORDER, "Streetview");
        menu.add(GROUP_ID, ITEMWAYPOINTLIST_ID, ORDER, "Liste waypoints");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case ITEMZOOMIN_ID:
                Log.i("DF", "Zoom in");
                mMap.moveCamera(CameraUpdateFactory.zoomBy(10));
                break;
            case ITEMZOOMOUT_ID:
                Log.i("DF", "Zoom out");
                mMap.moveCamera(CameraUpdateFactory.zoomBy(-10));
                break;
            case ITEMSAT_ID:
                Log.i("DF", "Vue satellite");
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case ITEMTRAFIC_ID:
                if (modeTrafic)
                {
                    modeTrafic = false;
                }
                else
                {
                    modeTrafic = true;
                }
                Log.i("DF", "Vue trafic");
                mMap.setTrafficEnabled(modeTrafic);
                break;
            case ITEMSTREET_ID:
                Log.i("DF", "Streetview");
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case ITEMWAYPOINTLIST_ID:
                Intent intent = new Intent(this, WaypointListActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng paris = new LatLng(LATITUDE, LONGITUDE);
        //this.mMap.addMarker(new MarkerOptions().position(paris).title("Paris"));
        //this.mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));

        if (waypointID != null)
        {
            Log.i("BABA", waypointID);
            Log.i("BABA", dbAdapter.getWaypointNameByID(Integer.parseInt(waypointID)));
            String markerName = dbAdapter.getWaypointNameByID(Integer.parseInt(waypointID));
            LatLng newWaypoint = new LatLng( dbAdapter.getWaypointLatByID(Integer.parseInt(waypointID)), dbAdapter.getWaypointLonByID(Integer.parseInt(waypointID)));
            mMap.addMarker(new MarkerOptions().position(newWaypoint).title(markerName));

            //Cursor c = dbAdapter.getRouteFromWaypointID(Integer.parseInt(waypointID));
            //if (c.moveToFirst()) {
            //    do {
            //        LatLng newPoint = new LatLng(c.getFloat(c.getColumnIndex("way_lat")), c.getFloat(c.getColumnIndex("way_lon")));
            //        mMap.addMarker(new MarkerOptions().position(newPoint).title(markerName));
            //    } while(c.moveToNext());
            //}
            //c.close();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("called", "onLocationChanged");

        //when the location changes, update the map by zooming to the location
        //CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
        //this.mMap.moveCamera(center);
        Log.i("position", location.toString());
        LatLng mPos = new LatLng(location.getAltitude(), location.getLatitude());
        if (this.mMap != null)
        {
            //this.mMap.addMarker(new MarkerOptions().position(mPos).title("Nouveau"));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(mPos));
        }

        //CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        //this.mMap.animateCamera(zoom);
    }
}