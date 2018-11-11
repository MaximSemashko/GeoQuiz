package com.example.cobra.geoquiz;

public class Question {
    private int mTexResId;
    private boolean mAnswerTrue;

    public Question(int texResId, boolean answerTrue) {
        mTexResId = texResId;
        mAnswerTrue = answerTrue;
    }

    public Question() {
    }

    public int getTexResId() {
        return mTexResId;
    }

    public void setTexResId(int texResId) {
        mTexResId = texResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
