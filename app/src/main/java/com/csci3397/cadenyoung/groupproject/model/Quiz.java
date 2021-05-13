package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

import java.util.ArrayList;
public class Quiz {

    private ArrayList<Question> quizQuestions;
    private int currentQuestion;

    public Quiz()
    {
        quizQuestions = new ArrayList<Question> ();
        quizQuestions.add(new Question(R.string.question_instructions, ""));
        quizQuestions.add(new Question(R.string.question_1, "mental"));
        quizQuestions.add(new Question(R.string.question_2, "mental"));
        quizQuestions.add(new Question(R.string.question_3, "mental"));
        quizQuestions.add(new Question(R.string.question_4, "mental"));
        quizQuestions.add(new Question(R.string.question_5, "mental"));
        quizQuestions.add(new Question(R.string.question_6, "mental"));
        quizQuestions.add(new Question(R.string.question_7, "mental"));
        quizQuestions.add(new Question(R.string.question_8, "mental"));
        quizQuestions.add(new Question(R.string.question_9,"mental"));
        quizQuestions.add(new Question(R.string.question_10, "water"));
        quizQuestions.add(new Question(R.string.question_11, "fitness"));
        quizQuestions.add(new Question(R.string.question_12, "sleep"));
        quizQuestions.add(new Question(R.string.question_13, "eating"));
        quizQuestions.add(new Question(R.string.question_14, "eating"));
        currentQuestion = 0;

    }

    public Question getPage(int i) {
        if(i >= quizQuestions.size())
        {
            i = 0;
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
            currentQuestion = 0;
        }
        return quizQuestions.get(currentQuestion);

    }
    public ArrayList<Question> getQuestions()
    {
        return quizQuestions;
    }

    public Question previousQuestion()
    {
        if (currentQuestion <= 0 )
        {
            currentQuestion = 0;
        }
        else
        {
            currentQuestion -= 1;
        }
        return quizQuestions.get(currentQuestion);


    }
    public boolean isInstructionsQuestion()
    {
        return currentQuestion == 0;
    }

    public boolean isFinalQuestion()
    {
        return currentQuestion == quizQuestions.size() -1;
    }

    public int getQuestionNum()
    {
        return currentQuestion;
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