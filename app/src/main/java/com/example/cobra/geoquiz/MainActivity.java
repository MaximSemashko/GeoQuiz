package com.example.cobra.geoquiz;

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
    private static int CORRECT_ANSWERS=0;

    private Button mTrueButton;
    private Button mFalseButton;
    private TextView mQuestionText;
    private Button mNextButton;
    private Button mPrevButton;
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
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }


        Log.d(TAG,"onCreate called");
        mTrueButton=findViewById(R.id.true_button);
        mFalseButton=findViewById(R.id.false_button);
        mNextButton=findViewById(R.id.next_button);
        mPrevButton=findViewById(R.id.prev_button);
        mQuestionText=findViewById(R.id.question_text);

        updateQuestion();

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
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
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
    }

    //method to check answer
    private void checkAnswer(boolean userPressedTrue){
        boolean isAnswerTrue= mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if(userPressedTrue==isAnswerTrue) {
            messageResId = R.string.correct_toast;
            CORRECT_ANSWERS++;
        }
        else
            messageResId = R.string.incorrect_toast;

            Toast toast = Toast.makeText(MainActivity.this,messageResId,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        if(mCurrentIndex==mQuestionBank.length-1){
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mNextButton.setEnabled(false);
            mPrevButton.setEnabled(false);
            mQuestionText.setEnabled(false);
            double result = CORRECT_ANSWERS*100/mQuestionBank.length;
            Toast.makeText(MainActivity.this,"Your result: "+result+"%",Toast.LENGTH_LONG).show();
        }
    }
}
