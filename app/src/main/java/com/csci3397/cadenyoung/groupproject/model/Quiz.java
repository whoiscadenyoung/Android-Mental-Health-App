package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;
import java.util.ArrayList;
public class Quiz {

    private ArrayList<Question> quizQuestions;

    private int currentQuestion;

    public Quiz()
    {
        quizQuestions = new ArrayList<Question> ();
        quizQuestions.add(new Question(R.string.question_1, "emotion"));
        quizQuestions.add(new Question(R.string.question_2, "emotion"));
        quizQuestions.add(new Question(R.string.question_3, "emotion"));
        quizQuestions.add(new Question(R.string.question_4, "emotion"));
        quizQuestions.add(new Question(R.string.question_5, "emotion"));
//        quizQuestions.add(new Question(R.string.question_6, "emotion"));
//        quizQuestions.add(new Question(R.string.question_7, "emotion"));
//        quizQuestions.add(new Question(R.string.question_8, "emotion"));
//        quizQuestions.add(new Question(R.string.question_9,"emotion"));
//        quizQuestions.add(new Question(R.string.question_10, "water"));
//        quizQuestions.add(new Question(R.string.question_11, "fitness"));
//        quizQuestions.add(new Question(R.string.question_12, "sleep"));
//        quizQuestions.add(new Question(R.string.question_13, "eating"));
//        quizQuestions.add(new Question(R.string.question_14, "eating"));
        currentQuestion = -1;

    }

    public Question getPage(int i) {
        if(i >= quizQuestions.size())
        {
            i =0;
        }
        return quizQuestions.get(i);
    }

    public Question nextQuestion()
    {
        if (currentQuestion < quizQuestions.size())
        {
            currentQuestion += 1;
        }
        else
        {
            currentQuestion = -1;
        }
        return quizQuestions.get(currentQuestion);

    }

    public Question previousQuestion()
    {
        if (currentQuestion <= -1 )
        {
            currentQuestion -= 1;
        }
        else
        {
            currentQuestion = -1;
        }
        return quizQuestions.get(currentQuestion);


    }
    public boolean isInstructionsQuestion()
    {
        return currentQuestion == -1;
    }

    public boolean isFinalQuestion()
    {
        return currentQuestion == quizQuestions.size() -1;
    }

    public int getNextAnswer()
    {
        if (currentQuestion < quizQuestions.size() -1 )
        {
            return quizQuestions.get(currentQuestion + 1).getAnswer();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public String toString()
    {
        String temp = "";
        for (int i =0; i < quizQuestions.size(); i++)
        {
            temp += quizQuestions.get(i);
        }
        return temp;
    }

}