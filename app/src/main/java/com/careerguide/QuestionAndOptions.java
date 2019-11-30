package com.careerguide;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class QuestionAndOptions implements Serializable
{
    private Question question;
    private ArrayList<Option> options = new ArrayList<>();
    private String answerKey = "";


    public QuestionAndOptions(Question question, ArrayList<Option> options) {
        this.question = question;
        this.options = options;
    }

    public Question getQuestion() {
        return question;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }
}
