package com.csci3397.cadenyoung.groupproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.MainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Stat;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.User;
import com.csci3397.cadenyoung.groupproject.ui.statistics.StatisticsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class HomeFragment extends Fragment {

    private Button goToQuizBtn;
    private HomeViewModel homeViewModel;
    private Button logOutButton;
    private boolean setLastDay = false;
    private String lastDayTaken;
    private View root;
    private AlertDialogFragment dialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private int avatarID;
    private ImageView avatarImage;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new AlertDialogFragment();

        goToQuizBtn = root.findViewById(R.id.goToQuizBtn);
        goToQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    navigateToQuiz();
                } else {
                    alertUserError();
                }
            }
        });

        logOutButton = root.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //firebase sign out
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = df.format(calendar.getTime());
        homeViewModel.setDate(today);

        if(isNetworkAvailable()) {
            String userID = firebaseAuth.getUid();
            db = FirebaseDatabase.getInstance();
            myRef = db.getReference("users");
            Log.d("Checking userID in home fragment: ", userID);
            myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Log.d("Checking user in home fragment before: ", user.getName());
                    lastDayTaken = user.getLastDayTaken();
                    setLastDay = true;
                    avatarID = user.getAvatarID();
                    Log.d("Checking user in home fragment after: ", user.getName());
                    Log.d("Checking avatar id form database: ", String.valueOf(avatarID));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Database read from user in home", "unsuccessful");
                }
            });
        }


        setButtonVisibility();
        return root;
    }

    private void setButtonVisibility() {
        if(isNetworkAvailable()) {
            if (!setLastDay) {
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setButtonVisibility();
                    }
                }, 50);
            } else {
                if (lastDayTaken.equals(homeViewModel.getDate()))
                    goToQuizBtn.setVisibility(root.GONE);
//                Drawable image = ContextCompat.getDrawable(getContext(), avatarID);
//                avatarImage.setImageDrawable(image);

            }
        } else {
            goToQuizBtn.setVisibility(root.GONE);
        }
    }

    private void navigateToQuiz() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(
                R.id.action_navigation_home_to_quiz,
                null,
                new NavOptions.Builder()
                        .setEnterAnim(android.R.animator.fade_in)
                        .setExitAnim(android.R.animator.fade_out)
                        .build()
        );
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