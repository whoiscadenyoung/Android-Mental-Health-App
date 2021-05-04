package com.csci3397.cadenyoung.groupproject.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

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
    private boolean HasTakenQuizToday;
    private String lastDayTaken;
    View root;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();


        goToQuizBtn = root.findViewById(R.id.goToQuizBtn);
        goToQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToQuiz();
            }
        });


//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        logOutButton = root.findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //firebase sign out
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String today = df.format(calendar.getTime());
        homeViewModel.setDate(today);


        String userID = firebaseAuth.getUid();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("users");

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//                System.out.println(post);

                User user = snapshot.child(userID).getValue(User.class);
                lastDayTaken = user.getLastDayTaken();
                //lastDayTaken = snapshot.child("lastDayTaken").getValue().toString();
                Log.d("lastDayTaken is equal to" ,lastDayTaken);
                Toast.makeText(getContext(), lastDayTaken, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database read from user", "unsuccessul");
            }
        });
//TODO FIX THIS
//        if(lastDayTaken.equals(today)) goToQuizBtn.setVisibility(getView().GONE);

        return root;
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


//    private NavController getNavController() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.f);
//        if (!(fragment instanceof NavHostFragment)) {
//            throw new IllegalStateException("Activity " + this + " does not have a NavHostFragment");
//        }
//        return ((NavHostFragment) fragment).getNavController();
//    }
}