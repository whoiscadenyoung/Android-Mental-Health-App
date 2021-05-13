package com.csci3397.cadenyoung.groupproject.ui.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.csci3397.cadenyoung.groupproject.AlertDialogFragment;
import com.csci3397.cadenyoung.groupproject.HomeMainActivity;
import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Question;
import com.csci3397.cadenyoung.groupproject.model.Quiz;
import com.csci3397.cadenyoung.groupproject.model.Stats;
import com.csci3397.cadenyoung.groupproject.ui.home.HomeViewModel;
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
    private SeekBar progressBarAnswer;
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
        progressBarAnswer = (SeekBar)view.findViewById(R.id.questionAnswer);
        progressBarAnswer.setVisibility(view.GONE);
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
                        currentQuestion.setAnswer(progressBarAnswer.getProgress() + 1);
                    }
                    progressBarAnswer.setVisibility(view.VISIBLE);
                    Log.d("Current Question", Integer.toString(quiz.getQuestionNum()));
                    currentQuestion = quiz.nextQuestion();
                    quizViewModel.setText(getString(currentQuestion.getTextId()));
                    setProgressBar();
                    if (quiz.isFinalQuestion()) {
                        next.setText("Submit");
                    }
                }

                else {
                    if (((HomeMainActivity) getActivity()).isNetworkAvailable()) {
                        setLastDayTaken();
                        setToDB();
                        Log.d("Quiz Submitted", quiz.toString());
                        navigateToHome();
                    }
                    else {
                        ((HomeMainActivity) getActivity()).alertUserError(dialog);
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
                    if (quiz.getQuestionNum() == 0)
                    {
                        progressBarAnswer.setVisibility(view.GONE);
                    }
                    else
                    {
                        setProgressBar();
                    }
                }
                else
                {
                    navigateToHome();
                }
            }
        });
    }

    private void setToDB() {
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
                    stats.quizUpdate(quiz);
                    myRef.child(userID).setValue(stats);
                }
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
            progressBarAnswer.setProgress(0);
        }
        else
        {
            progressBarAnswer.setProgress(answer-1);
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

}