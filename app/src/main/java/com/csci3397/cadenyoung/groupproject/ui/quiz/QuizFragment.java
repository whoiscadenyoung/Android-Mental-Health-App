package com.csci3397.cadenyoung.groupproject.ui.quiz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Question;
import com.csci3397.cadenyoung.groupproject.model.Quiz;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.model.User;
import com.csci3397.cadenyoung.groupproject.model.UserStats;
import com.csci3397.cadenyoung.groupproject.ui.home.HomeViewModel;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private QuizViewModel quizViewModel;
    private Button back;
    private Button next;
    private Quiz quiz;
    private Question currentQuestion;
    private View view;
    private SeekBar progessBarAnswer;
    private HomeViewModel homeViewModel;
    private AlertDialogFragment dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        dialog = new AlertDialogFragment();
        db = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        quizViewModel =  ViewModelProviders.of(requireActivity()).get(QuizViewModel.class);
        view = inflater.inflate(R.layout.fragment_quiz, container, false);
        final TextView textView = view.findViewById(R.id.question_view);
        quizViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        quizViewModel.setText(getString(R.string.question_instructions));
        next = view.findViewById(R.id.next);
        back = view.findViewById(R.id.back);
        progessBarAnswer = (SeekBar)view.findViewById(R.id.questionAnswer);
        progessBarAnswer.setVisibility(view.GONE);
        quiz = new Quiz();
        loadButtons();
        return view;
    }

    private void loadButtons() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!quiz.isFinalQuestion()) {
                    if (!quiz.isInstructionsQuestion()) {
                        currentQuestion.setAnswer(progessBarAnswer.getProgress());
                    }
                    progessBarAnswer.setVisibility(view.VISIBLE);
                    Log.d("Current Question", Integer.toString(quiz.getQuestionNum()));
                    currentQuestion = quiz.nextQuestion();
                    quizViewModel.setText(getString(currentQuestion.getTextId()));
                    setProgressBar();
                    if (quiz.isFinalQuestion()) {
                        next.setText("Submit");
                    }
                }

                else {
                    Log.d("Before Network Check", "got here");
                    if (isNetworkAvailable()) {
                        setLastDayTaken();
//                        quizViewModel.setQuiz(quiz);
                        setToDB();
                        Log.d("Quiz Submitted", quiz.toString());
                        navigateToHome();

                        //TODO have the submit button take you back to the home page
                    }
                    else {
                        alertUserError();
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quiz.isInstructionsQuestion())
                {
                    currentQuestion = quiz.previousQuestion();
                    quizViewModel.setText(getString(currentQuestion.getTextId()));
                    next.setText("Next");
                    if (quiz.getQuestionNum() == 1)
                    {
                        progessBarAnswer.setVisibility(view.GONE);
                    }
                    else
                    {
                        setProgressBar();
                    }
                }
                else
                {
                    // Firebase needs to pull the stats from the database
                    // stats.updateFromQuiz(quiz)
                    navigateToHome();
                }
                //TODO create a way to back to the saved instance of the question

            }
        });
    }

    private void setToDB() {
        String userID = firebaseAuth.getUid();
        myRef = db.getReference("stats");

        //Read user's current stats from database
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Get user info
                UserStats userStats = snapshot.getValue(UserStats.class);
                Log.d("Checking Quiz Dtabase: ", String.valueOf(userStats.getStat1progress()));
                Stats stats = new Stats(userStats);
                //Update stats
                userStats = stats.updateFromQuiz(userID, quiz);
                Log.d("Checking Quiz Dtabase: ", "After Update: " + String.valueOf(userStats.getStat1progress()));
                //Set new stats to database
                myRef.child(userID).setValue(userStats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Database read from user in quiz", "unsuccessful");
            }
        });
    }

    private void setLastDayTaken() {
        myRef = db.getReference("users");// path to the date stuff
        String userID = firebaseAuth.getUid();
        myRef.child(userID).child("lastDayTaken").setValue(homeViewModel.getDate());
    }

    private void setProgressBar()
    {
        int answer = currentQuestion.getAnswer();
        if (answer == -1)
        {
            progessBarAnswer.setProgress(0);
        }
        else
        {
            progessBarAnswer.setProgress(answer);
        }
    }

    private void navigateToHome() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(
                R.id.action_navigation_quiz_to_home,
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