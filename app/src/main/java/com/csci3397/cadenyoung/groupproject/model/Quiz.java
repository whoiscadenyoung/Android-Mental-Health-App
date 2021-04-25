package com.csci3397.cadenyoung.groupproject.model;

import com.csci3397.cadenyoung.groupproject.R;

public class Quiz {

    private Question[] quizQuestions;
    private int currentQuestion;

    public Quiz()
    {
        quizQuestions[0] = new Question(R.string.question_1);
        quizQuestions[1] = new Question(R.string.question_2);
        quizQuestions[2] = new Question(R.string.question_3);
        quizQuestions[3] = new Question(R.string.question_4);
        quizQuestions[4] = new Question(R.string.question_5);
        quizQuestions[5] = new Question(R.string.question_6);
        quizQuestions[6] = new Question(R.string.question_7);
        quizQuestions[7] = new Question(R.string.question_8);
        quizQuestions[8] = new Question(R.string.question_9);
        quizQuestions[9] = new Question(R.string.question_10);
        quizQuestions[10] = new Question(R.string.question_11);
        quizQuestions[11] = new Question(R.string.question_12);

        currentQuestion = -1;

    }

    public Question getPage(int i) {
        if(i >= quizQuestions.length)
        {
            i =0;
        }
        return quizQuestions[i];
    }

    public Question nextQuestion()
    {
        currentQuestion += 1;
        return quizQuestions[currentQuestion];

    }

}
