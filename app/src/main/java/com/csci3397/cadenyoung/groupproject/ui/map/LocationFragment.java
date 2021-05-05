package com.csci3397.cadenyoung.groupproject.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Location;
import com.csci3397.cadenyoung.groupproject.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LocationFragment extends Fragment {
    private FusedLocationProviderClient client;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    //@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        if (isNetworkAvailable()) {
            //Initialize location client
            client = client = LocationServices.getFusedLocationProviderClient(getActivity());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                //Has permission
                //Call method
                getCurrentLocation();
            } else {
                //Does not have permission
                //Request permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        } else {
            Toast.makeText(getActivity(), "Network unavailable", Toast.LENGTH_SHORT);
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //Has permission
            //Call method
            getCurrentLocation();
        } else {
            //Does not have permission
            //Display toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //When location service is enabled
            //Get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    //Initialize location
                    android.location.Location location = task.getResult();
                    //Check condition
                    if (location != null) {
                        //When location not null
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        setNewLocation(loc);

                    } else {
                        //When location is null
                        //Initialize location request
                        LocationRequest locationRequest = new LocationRequest().create()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //Initialize location
                                android.location.Location location1 = locationResult.getLastLocation();
                                LatLng loc1 = new LatLng(location1.getLatitude(), location1.getLongitude());
                                setNewLocation(loc1);
                            }
                        };
                        //Request location updates
                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });

        } else {
            //When location service is not enabled
            //Open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void setNewLocation(LatLng loc) {
        //Initialize map fragment
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //Initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();
                if (loc != null) {
                    //Add location to db
                    addToDB(loc);
                    //Set position of marker
                    markerOptions.position(loc);
                    //Set title of marker
                    markerOptions.title("Current location");
                    //Set user avatar
                    //TODO
                    //Read from users
                    String userID = firebaseAuth.getUid();
                    db = FirebaseDatabase.getInstance();
                    myRef = db.getReference("users");

                    myRef.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Log.d("user is not null", user.getName());
                            int avatar = user.getAvatarID();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("Database read from user", "unsuccessful");
                        }
                    });



//                    private fun loadMarkersFromDB(){
//                        val query = database.getReference("spots/").orderByChild("place/latLng")
//                        query.addListenerForSingleValueEvent(object: ValueEventListener{
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                if(dataSnapshot.exists()){
//                                    var lat: Double
//                                    var lng: Double
//                                    var position: LatLng
//                                    for(spotLatLng:DataSnapshot in dataSnapshot.children){
//                                        lat = spotLatLng.child("place/latLng/latitude/").value.toString().toDouble()
//                                        lng = spotLatLng.child("place/latLng/longitude/").value.toString().toDouble()
//                                        position = LatLng(lat, lng)
//                                        Log.d(TAG, "Lat: ${position.latitude} Lng: ${position.longitude}")
//                                    }
//                                }
//                            }
//                            override fun onCancelled(p0: DatabaseError) {
//                            }
//                        } )
//                    }

                    myRef = db.getReference("locations");

                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String uID = snapshot.getKey();
                            if (!uID.equals(userID)) {
                                //User user = snapshot.getValue(User.class);
                                Location childLocation = snapshot.getValue(Location.class);
                                Log.d("CHILD LOCATION IS NOT NULL", uID);
                                Double cLat = childLocation.getLatitude();
                                Double cLng = childLocation.getLongitude();
                                Log.d("LAT THING", String.valueOf(cLat));
                                googleMap.addMarker(markerOptions.position(new LatLng(cLat, cLng)));

                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChild) { }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }

                    });

                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.robot));
                    //Remove all markers
                    googleMap.clear();
                    //Animate to zoom on the marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            loc, 10
                    ));
                } else {
                    Toast.makeText(getActivity(), "Error finding current location", Toast.LENGTH_SHORT).show();
                }
                //Add marker on the map
                googleMap.addMarker(markerOptions);
            }
        });
    }

    private void addToDB(LatLng loc) {
        //Insert current user's last known location into locations table
        //Create table for locations
        db = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = db.getReference("locations");
        String userID = firebaseAuth.getUid();
        Location lastLoc = new Location(loc.latitude, loc.longitude);
        myRef.child(userID).setValue(lastLoc);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean isAvailable = false;

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isAvailable = true;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isAvailable = true;
            }
        } else {
            Toast.makeText(getActivity(), "Sorry, network is not available",
                    Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }

//    private OnMapReadyCallback callback = new OnMapReadyCallback() {
//
//        @Override
//        public void onMapReady(GoogleMap googleMap) {
//            String userID = firebaseAuth.getUid();
//            db = FirebaseDatabase.getInstance();
//            myRef = db.getReference("locations");
//
//            myRef.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    String uID = snapshot.getKey();
//                    if (!uID.equals(userID)) {
//                        Location childLocation = snapshot.getValue(Location.class);
//                        LatLng loc = childLocation.getLastLocation();
//                        googleMap.addMarker(new MarkerOptions().position(loc));
//
//                    }
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChild) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//
//            });
//        }
//    };
}
