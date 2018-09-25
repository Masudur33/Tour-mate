package com.example.masud.mytourassociator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener{

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude,longitude;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            checkLocationPermission();
        }

        if (!CheckGooglePlayServices()){
            Log.d("onCreate", "Finishing test case since Google Play service are not available");
            finish();
        }
        else{

            Log.d("onCreate", "Google Play service  available");

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


private boolean CheckGooglePlayServices(){

    GoogleApiAvailability googleAPI=GoogleApiAvailability.getInstance();
    int result=googleAPI.isGooglePlayServicesAvailable(this);
    if (result != ConnectionResult.SUCCESS){
        if (googleAPI.isUserResolvableError(result))
        {
            googleAPI.getErrorDialog(this,result,0).show();
        }
        return false;
    }
    return true;
}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
           protected synchronized void buildGoogleApiClient(){
            mGoogleApiClient=new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();

        }
        public void onClick(View v)
        {
            Object dataTransfer[]=new Object[2];
            GetnearbyPlacesData getnearbyPlacesData=new GetnearbyPlacesData();

          switch (v.getId()){
              case R.id.B_search: {
                  EditText tf_location=(EditText)findViewById(R.id.TF_location);
                  String location =tf_location.getText().toString();
                  List<Address>addressList=null;
                  MarkerOptions markerOptions=new MarkerOptions();

                  if (!location.equals("")){
                      Geocoder geocoder=new Geocoder(this);
                      try {
                          addressList=geocoder.getFromLocationName(location,5);
                      } catch (IOException e) {
                          e.printStackTrace( );
                      }

                      for (int i=0;i<addressList.size();i++){

                          Address myAddress=addressList.get(i);
                          LatLng latLng=new LatLng(myAddress.getLatitude(),myAddress.getLongitude());
                          markerOptions.position(latLng);
                          mMap.addMarker(markerOptions);
                          mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                      }
                  }
                  }
                 break;
              case R.id.hospita_btn:
              mMap.clear();
              String hospital="hospital";
              String url=getUrl(latitude,longitude,hospital);

              dataTransfer[0]=mMap;
              dataTransfer[1]=url;

              getnearbyPlacesData.execute(dataTransfer);
                  Toast.makeText(MapsActivity.this,"Showing nearby Hospitals",Toast.LENGTH_LONG).show();
                  break;

              case R.id.resturent_btn:
                  mMap.clear();
                  String restaurant="restaurant";
                  url=getUrl(latitude,longitude,restaurant);
                  dataTransfer[0]=mMap;
                  dataTransfer[1]=url;
                  getnearbyPlacesData.execute(dataTransfer);
                  Toast.makeText(MapsActivity.this,"Showing nearby restaurants",Toast.LENGTH_LONG).show();
                  break;

              case R.id.atm_btn:
                  mMap.clear();
                  String atmBooth="atm booths";
                  url=getUrl(latitude,longitude,atmBooth);
                  dataTransfer[0]=mMap;
                  dataTransfer[1]=url;
                  getnearbyPlacesData.execute(dataTransfer);
                  Toast.makeText(MapsActivity.this,"Showing nearby ATM Booths",Toast.LENGTH_LONG).show();
                  break;

              case R.id.hotel_btn:
                  mMap.clear();
                  String hotels="hotels";
                  url=getUrl(latitude,longitude,hotels);
                  dataTransfer[0]=mMap;
                  dataTransfer[1]=url;
                  getnearbyPlacesData.execute(dataTransfer);
                  Toast.makeText(MapsActivity.this,"Showing nearby Hotels",Toast.LENGTH_LONG).show();
                  break;
          }
        }

        private String getUrl(double latitude, double longitude, String nearbyPlace) {
            StringBuilder googlePlaceUrl=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlaceUrl.append("location"+latitude+","+longitude);
            googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
            googlePlaceUrl.append("&type="+nearbyPlace);
            googlePlaceUrl.append("&sensor=true");
            googlePlaceUrl.append("&key="+"AIzaSyCBX0B6s2fU2-akbH_OcZ-Gd-QLsrDo9To");

            return googlePlaceUrl.toString();
        }

        public void onConnected(Bundle bundle){
            mLocationRequest=new LocationRequest( );
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LOCATION_SERVICE.FusedLocationApi.requestLocationUpdates(mGoogleApiClient.mLocationRequest, this);
            }
        }
        @Override
        public void onConnectionSuspended(int i) {
        }

    }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}
