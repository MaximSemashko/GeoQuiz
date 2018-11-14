package com.example.cobra.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.cobra.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.cobra.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";

    private Boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mApiLevel;
    private static final String ANSWER_INDEX="index";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle onSaveInstanceState) {
        super.onSaveInstanceState(onSaveInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        onSaveInstanceState.putBoolean(ANSWER_INDEX,mAnswerIsTrue);
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        mAnswerTextView=findViewById(R.id.answer_text_view);
        mShowAnswerButton=findViewById(R.id.answer_show_button);
        mApiLevel = findViewById(R.id.api_level);

        //add Build version to textView
        mApiLevel.setText("API level: "+Build.VERSION.SDK_INT);
        if (savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(ANSWER_INDEX,false);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAnswerIsTrue)
                    mAnswerTextView.setText(R.string.true_button);
                else
                    mAnswerTextView.setText(R.string.false_button);
                setAnswerShownResult(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }
                else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

}
