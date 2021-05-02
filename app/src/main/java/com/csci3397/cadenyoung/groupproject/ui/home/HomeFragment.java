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
import com.csci3397.cadenyoung.groupproject.ui.statistics.StatisticsViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {

    private Button goToQuizBtn;
    private HomeViewModel homeViewModel;
    private Button logOutButton;
    FirebaseAuth firebaseAuth;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


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
                firebaseAuth = FirebaseAuth.getInstance();
                //firebase sign out
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //intent.putExtra("name",name);
                startActivity(intent);
            }
        });

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