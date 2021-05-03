package com.csci3397.cadenyoung.groupproject.ui.quiz;

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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.csci3397.cadenyoung.groupproject.R;
import com.csci3397.cadenyoung.groupproject.model.Question;
import com.csci3397.cadenyoung.groupproject.model.Quiz;

import static android.content.ContentValues.TAG;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;
    private Button back;
    private Button next;
    private Quiz quiz;
    private Question currentQuestion;
    private View view;
    private SeekBar seekBarAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
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
        seekBarAnswer = (SeekBar)view.findViewById(R.id.questionAnswer);

        quiz = new Quiz();
        loadButtons();
        return view;
    }

    private void loadFirstQuestion()
    {
        quizViewModel.setText(getString(quiz.nextQuestion().getTextId()));
    }

    private void loadButtons() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quiz.isFinalQuestion())
                {
                    currentQuestion = quiz.nextQuestion();
                    int tempAnswer = seekBarAnswer.getProgress();
                    currentQuestion.setAnswer(tempAnswer);
                    //Toast.makeText(getActivity(), currentQuestion.getAnswer() + "", Toast.LENGTH_SHORT).show();
                    quizViewModel.setText(getString(currentQuestion.getTextId()));
                    setProgressBar();
                }

                else
                {

                    next.setText("Submit");
                    navigateToHome();
                    Log.d("Quiz Submitted", quiz.toString());
                    //TODO have the submit button take you back to the home page
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
                    setProgressBar();
                }
                else
                {
                    navigateToHome();
                }
                //TODO create a way to back to the saved instance of the question

            }
        });
    }

    private void setProgressBar()
    {
        int nextAnswer = quiz.getNextAnswer();
        if (nextAnswer == -1)
        {
            seekBarAnswer.setProgress(0);
        }
        else
        {
            seekBarAnswer.setProgress(nextAnswer);
        }
    }
//stuff changed
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