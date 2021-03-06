package com.example.cobra.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX_TASK = "index";

    private static int CHEAT_COUNT = 3;

    private static int CORRECT_ANSWERS=0;
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;


    //Check if we visite CheatActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionText;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mCheatingCounter;

    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Safe instance after rotation
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_INDEX_TASK,false);
        }


        Log.d(TAG,"onCreate called");
        mTrueButton=findViewById(R.id.true_button);
        mFalseButton=findViewById(R.id.false_button);
        mNextButton=findViewById(R.id.next_button);
        mPrevButton=findViewById(R.id.prev_button);
        mQuestionText=findViewById(R.id.question_text);
        mCheatButton=findViewById(R.id.cheat_button);
        mCheatingCounter =findViewById(R.id.cheat_count);

        updateQuestion();

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent,REQUEST_CODE_CHEAT);

            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsCheater = false;
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsCheater = false;
                mCurrentIndex=(mCurrentIndex-1)%mQuestionBank.length;
                updateQuestion();
            }
        });

        //scrolling by textview
        mQuestionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy called");
    }

    //Save values in Bundle
    @Override
    public void onSaveInstanceState(Bundle onSaveInstanceState) {
        super.onSaveInstanceState(onSaveInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        onSaveInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        onSaveInstanceState.putBoolean(KEY_INDEX_TASK,mIsCheater);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart called");
    }


    //method to scroll questions:
    private void updateQuestion(){
        int question=mQuestionBank[mCurrentIndex].getTexResId();
        mQuestionText.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);

        //if user dont answer the question - enable button
        mNextButton.setEnabled(false);
        mPrevButton.setEnabled(false);
    }

    //method to check answer
    private void checkAnswer(boolean userPressedTrue){
        boolean isAnswerTrue= mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            //limit cheating
            CHEAT_COUNT--;
            String counter = String.valueOf(CHEAT_COUNT);
            mCheatingCounter.setText(counter);
            if(CHEAT_COUNT==0)
                mCheatButton.setEnabled(false);

            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == isAnswerTrue) {
                messageResId = R.string.correct_toast;
                CORRECT_ANSWERS++;
            } else
                messageResId = R.string.incorrect_toast;
        }
            Toast toast = Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        mNextButton.setEnabled(true);
        mPrevButton.setEnabled(true);

        //If last element - count result
        if(mCurrentIndex==mQuestionBank.length-1){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mNextButton.setEnabled(false);
            mPrevButton.setEnabled(false);
            mCheatButton.setEnabled(false);
            mQuestionText.setEnabled(false);
            double result = CORRECT_ANSWERS*100/mQuestionBank.length;
            Toast.makeText(MainActivity.this,"Your result: "+result+"%",Toast.LENGTH_LONG).show();
        }
    }
}
