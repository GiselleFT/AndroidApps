package com.movil.android.mydoctor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VerFarmaciasActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    String mensaje1;
    String direccion = "";

    //Marcadores para las farmacias
    private Marker[] placeMarkers;
    //Por defecto la API de Google places devuelve 20
    private final int MAX_PLACES = 20;
    //Para configurar los detalles de los marcadores
    private MarkerOptions[] places;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_farmacias);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        placeMarkers = new Marker[MAX_PLACES];

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //UiSettings uiSettings = mMap.getUiSettings();
        //uiSettings.setZoomControlsEnabled(true);
        //uiSettings.setMyLocationButtonEnabled(true);
        miUbicacion();
    }

    //Activar servicios de GPS cuando estén apagados
    private void locationStart(){
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnable = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnable){
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    public void setLocation(Location loc){
        if(loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list =  geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if(!list.isEmpty()){
                    Address DirCalle = list.get(0);
                    direccion = (DirCalle.getAddressLine(0));
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void agregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (marcador != null) {
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Ubicación Actual: " + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        //mMap.moveCamera(miUbicacion);
        mMap.animateCamera(miUbicacion);
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            agregarMarcador(lat, lng);

            /*Peticion lugar: Farmacias*/
            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                    "json?location="+lat+","+lng+
                    "&radius=5000&sensor=true" +
                    "&types=pharmacy"+
                    "&key=AIzaSyBtQ6U2mlincuHxUzqOGRmZcPre9ZssesI";

            new GetPlaces().execute(placesSearchStr);

        }
    }

    /*LocationListener tiene la funcion de estar siempre atento a cualquier
    cambio de localidad recibido por el GPS del dispositivo
    */
    LocationListener locListener = new LocationListener() {

        /*Se lanza cada vez que se recibe una actualizacion de posición*/
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            mensaje1 = ("GPS Activado");
            mensaje();
        }

        @Override
        public void onProviderDisabled(String provider) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            mensaje();
        }
    };

    /*Se hace referencia a la clase LocationManager, la cual es utilizada para obtener servicios de geo posicionamiento
     * en el dispositivo*/
    private static int PETICION_PERMISO_LOCALIZACION = 101;
    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        }
        else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            actualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,0, locListener);
        }


    }


    public void mensaje(){
        Toast toast = Toast.makeText(this, mensaje1, Toast.LENGTH_LONG);
        toast.show();
    }


    private class GetPlaces extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... placesURL) {
            StringBuilder placesBuilder = new StringBuilder();

            for (String placeSearchURL : placesURL){
                HttpClient placesClient = new DefaultHttpClient();
                try{
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    if(placeSearchStatus.getStatusCode() == 200){
                        HttpEntity placesEntity = placesResponse.getEntity();
                        InputStream placesContent = placesEntity.getContent();
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        String lineIn;
                        while((lineIn = placesReader.readLine()) != null){
                            placesBuilder.append(lineIn);
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //retornar los datos JSON
            return placesBuilder.toString();
        }

        protected void onPostExecute(String result){
            //Eliminar marcadores antiguios, cada que se modifique la aubicacion actual
            if(placeMarkers != null){
                for (int pm = 0; pm<placeMarkers.length; pm++){
                    if(placeMarkers[pm] != null){
                        placeMarkers[pm].remove();
                    }
                }
            }

            try{
                JSONObject resultObject = new JSONObject(result);
                JSONArray placesArray = resultObject.getJSONArray("results");
                //Se crea un marcador por cada tipo de lugar devuelto
                places = new MarkerOptions[placesArray.length()];
                for (int p=0; p<placesArray.length(); p++){
                    boolean missingValue = false;
                    //creacion de variables para cada aspecto de lugar que se requiere recuperar y pasarlas al marcador
                    LatLng placeLL = null;
                    String placeName ="";
                    String vicinity = "";
                    int currIcon = R.mipmap.ic_launcher;

                    JSONObject placeObject = placesArray.getJSONObject(p);
                    //se recupera lat y long (ubicacion)
                    JSONObject loc = placeObject.getJSONObject("geometry").getJSONObject("location");
                    placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                        Double.valueOf(loc.getString("lng")));
                    //se obtiene el tipo de lugar
                    JSONArray types = placeObject.getJSONArray("types");
                    for (int t=0; t<types.length(); t++){
                        String thisType = types.get(t).toString();
                    }
                    //recuperar datos de proximidad
                    vicinity = placeObject.getString("vicinity");
                    //recuperar nombre de lugar
                    placeName = placeObject.getString("name");

                    //creacion del marcador
                    places[p] = new MarkerOptions()
                            .position(placeLL)
                            .title(placeName)
                            .icon(BitmapDescriptorFactory.fromResource(currIcon))
                            .snippet(vicinity);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //se recorre el conjunto de markeroptions, se instancia un marcador para cada uno
            //agregandolo al mapa y almacenando una referencia en el arreglo
            if(places != null && placeMarkers != null){
                for (int p=0; p<places.length && p<placeMarkers.length; p++){
                    if(places[p] != null){
                        placeMarkers[p] = mMap.addMarker(places[p]);
                    }
                }
            }
        }
    }

}

