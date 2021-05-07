package com.csci3397.cadenyoung.groupproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.MainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.User;
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
    private DatabaseReference userRef;
    private DatabaseReference statsRef;
    private int defaultAvatar;
    private ImageView avatarView;
    private RadioGroup radioGroup;
    private DatabaseReference myRef;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new AlertDialogFragment();

        defaultAvatar = R.drawable.fitness;
        avatarView = root.findViewById(R.id.avatarImage);
        avatarView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), defaultAvatar, null));

        goToQuizBtn = root.findViewById(R.id.goToQuizBtn);
        goToQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((HomeMainActivity) getActivity()).isNetworkAvailable()) {
                    navigateToQuiz();
                } else {
                    ((HomeMainActivity) getActivity()).alertUserError(dialog);
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

        RadioButton greenButton = root.findViewById(R.id.greenButton);
        RadioButton yellowButton = root.findViewById(R.id.yellowButton);

        greenButton.setOnClickListener(v -> {
            boolean checked = ((RadioButton) v).isChecked();

            // Check which radio button was clicked
            switch(v.getId()) {
                case R.id.greenButton:
                    if (checked)
                        setToDB(1);
                    break;
            }
        });

        yellowButton.setOnClickListener(v -> {
            boolean checked = ((RadioButton) v).isChecked();

            // Check which radio button was clicked
            switch(v.getId()) {
                case R.id.yellowButton:
                    if (checked)
                        setToDB(2);
                    break;
            }
        });

        /*radioGroup = root.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.greenButton:
                        setToDB(1);
                        break;
                    case R.id.yellowButton:
                        setToDB(2);
                        break;
                }
            }
        });*/

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = df.format(calendar.getTime());
        homeViewModel.setDate(today);

        if(((HomeMainActivity) getActivity()).isNetworkAvailable()) {
            String userID = firebaseAuth.getUid();
            db = FirebaseDatabase.getInstance();
            userRef = db.getReference("users");
            statsRef = db.getReference("stats");

            userRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user == null) {
                        lastDayTaken = today;
                    } else {
                        lastDayTaken = user.getLastDayTaken();
                        setLastDay = true;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Database read from user in home", "unsuccessful");
                }
            });

            statsRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Stats stats = snapshot.getValue(Stats.class);
                    String avatarPath = stats.returnAvatarPath();
                    if (avatarPath != null) {
                        //root.getContext().getPackageName()
                        int avatarId = root.getResources().getIdentifier(avatarPath, "drawable", "com.csci3397.cadenyoung.groupproject");
                        avatarView.setImageDrawable(ResourcesCompat.getDrawable(root.getResources(), avatarId, null));
                    }
                    int avatarType = stats.getAvatarType();
                    if (avatarType == 2) radioGroup.check(R.id.yellowButton);
                    else radioGroup.check(R.id.greenButton);
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
        if(((HomeMainActivity) getActivity()).isNetworkAvailable()) {
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

    private void setToDB(int avatarId) {
        String userID = firebaseAuth.getUid();
        myRef = db.getReference("stats");

        //Read user's current stats from database

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Stats stats = snapshot.getValue(Stats.class);
                if (stats == null) {
                    Log.d("Null Stats Object", "Can't get the Stats object from database");
                } else {
                    stats.setAvatarType(avatarId);
                    myRef.child(userID).setValue(stats);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database read from user in quiz", "unsuccessful");
            }
        });
    }

    public void clickRadio(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.greenButton:
                if (checked)
                    setToDB(1);
                    break;
            case R.id.yellowButton:
                if (checked)
                    setToDB(R.id.yellowButton);
                    break;
        }
    }

}