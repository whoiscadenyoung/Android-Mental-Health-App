package com.csci3397.cadenyoung.groupproject.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.csci3397.cadenyoung.groupproject.HomeMainActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Location;
import com.csci3397.cadenyoung.groupproject.model.Stats;
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


public class MapFragment extends Fragment {
    private FusedLocationProviderClient client;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Initialize view
        view = inflater.inflate(R.layout.fragment_map, container, false);

        if (((HomeMainActivity) getActivity()).isNetworkAvailable()) {
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

                    //Read current user from users
                    String userID = firebaseAuth.getUid();
                    db = FirebaseDatabase.getInstance();
                    myRef = db.getReference("stats");

                    myRef.child(userID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Get user info
                            Stats userStats = snapshot.getValue(Stats.class);
                            //Get user avatar
                            String avatarPath = userStats.returnAvatarPath();
                            int avatarId = view.getResources().getIdentifier(avatarPath, "drawable", view.getContext().getPackageName());
                            ScaleDrawable img = new ScaleDrawable(ResourcesCompat.getDrawable(view.getResources(), avatarId, null), 0, (float) 0.1, (float) 0.1);
                            //Set user marker
                            googleMap.addMarker(markerOptions.position(loc).title("Current Location").icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable) img.getDrawable()).getBitmap())));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("Database read from user in user location", "unsuccessful");
                        }
                    });

                    //Read other users' locations from database and add them as markers in map
                    myRef = db.getReference("locations");
                    myRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String uID = snapshot.getKey();
                            if (!uID.equals(userID)) {
                                Location childLocation = snapshot.getValue(Location.class);
                                Double cLat = childLocation.getLatitude();
                                Double cLng = childLocation.getLongitude();
                                //Read avatar of child from database
                                DatabaseReference childRef = db.getReference("stats");

                                childRef.child(uID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Stats childStats = snapshot.getValue(Stats.class);
                                        //Get child avatar
                                        String childAvatar = childStats.returnAvatarPath();
                                        int childAvatarId = view.getResources().getIdentifier(childAvatar, "drawable", view.getContext().getPackageName());
                                        ScaleDrawable img = new ScaleDrawable(ResourcesCompat.getDrawable(view.getResources(), childAvatarId, null), 0, (float) 0.5, (float) 0.5);
                                        //Set child marker
                                        googleMap.addMarker(markerOptions.position(new LatLng(cLat, cLng)).title("Random User Location").icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable) img.getDrawable()).getBitmap())));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("Database read from user in child location", "unsuccessful");
                                    }
                                });

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

                    //Remove all markers
                    googleMap.clear();
                    //Animate to zoom on the marker
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            loc, 10
                    ));
                } else {
                    Toast.makeText(getActivity(), "Error finding current location", Toast.LENGTH_SHORT).show();
                }
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

}
