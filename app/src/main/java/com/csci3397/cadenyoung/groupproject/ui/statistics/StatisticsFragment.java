package com.csci3397.cadenyoung.groupproject.ui.statistics;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.CustomAdapter;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.UserStats;
import com.csci3397.cadenyoung.groupproject.ui.home.HomeViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomAdapter adaptor;
    private TableLayout tableLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private Stats stats;
    private boolean setStats = false;
    private AlertDialogFragment dialog;
    private  View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = ViewModelProviders.of(requireActivity()).get(StatisticsViewModel.class);
        root = inflater.inflate(R.layout.fragment_statistics, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        String userID = firebaseAuth.getUid();
        myRef = db.getReference("stats");


        //Read user's current stats from database
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get user info
                UserStats userStats = snapshot.getValue(UserStats.class);

                //Set the progress to current user stats
                stats = new Stats(userStats);
                setStats = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database read from user in stats", "unsuccessful");
            }
        });

        setStatsView();
        return root;
    }

    private void setStatsView() {
        //if(isNetworkAvailable()) {
            if (!setStats) {
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setStatsView();
                    }
                }, 50);
            }else{
                recyclerView = root.findViewById(R.id.recyclerView);
                layoutManager = new LinearLayoutManager(getActivity());
                adaptor = new CustomAdapter(stats.getStats());
                recyclerView.setAdapter(adaptor);
                recyclerView.setLayoutManager(layoutManager);
            }
       // } else {
        //    goToQuizBtn.setVisibility(root.GONE);
       // }
    }

    private void alertUserError() {
        dialog.show(getActivity().getSupportFragmentManager(), "error dialog");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean isAvailable = false;

        if(networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isAvailable = true;
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isAvailable = true;
            }
        } else {
            Toast.makeText(getActivity(),"Sorry, network is not available",
                    Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }
}