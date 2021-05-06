package com.csci3397.cadenyoung.groupproject.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.CustomAdapter;
import com.csci3397.cadenyoung.groupproject.model.Stats;
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
    private Stats stats;
    private boolean setStats = false;
    private AlertDialogFragment dialog;
    private  View root;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = ViewModelProviders.of(requireActivity()).get(StatisticsViewModel.class);
        root = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Dummy stats
        stats = new Stats(0);

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        String userID = firebaseAuth.getUid();
        dbReference = FirebaseDatabase.getInstance().getReference("stats");

        // View Elements
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        adaptor = new CustomAdapter(stats);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutManager(layoutManager);

        //Read user's current stats from database
        dbReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Get user info
                // UserStats userStats = dataSnapshot.getValue(UserStats.class);
                Stats newStats = dataSnapshot.getValue(Stats.class);
                if (stats == null) {
                    Log.d("Null UserStats Object", "Can't get the userStats object from database");
                } else {
                    //Set the progress to current user stats
                    Log.d("BEEP", "Made it. Got a userstats from the datasnapShot");
                    stats.replaceStats(newStats);
                    adaptor.notifyDataSetChanged();
                    root.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database read from user in stats failed", error.toString());
            }
        });

        return root;
    }

}