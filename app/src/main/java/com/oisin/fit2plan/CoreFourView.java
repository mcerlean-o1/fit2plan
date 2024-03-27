package com.oisin.fit2plan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoreFourView extends FrameLayout {

    ImageButton goodnessIn, hydration, movement, sleep;
    ProgressBar progressBar;
    TextView progressTxt;


    private final boolean[] buttonClicked = {false, true};
    private int currentProgress = 0;



    //This constructor needs context
    public CoreFourView(@NonNull Context context) {
        super(context);
        init();
    }

    //This sets the attributes
    public CoreFourView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CoreFourView);

        int progress = typedArray.getInteger(R.styleable.CoreFourView_progressAndText,0);
        setProgressAndText(String.valueOf(progress));
    }

    public CoreFourView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.core_four_view, this);

        goodnessIn = findViewById(R.id.goodness_in);
        hydration = findViewById(R.id.hydration);
        movement = findViewById(R.id.fitness);
        sleep = findViewById(R.id.sleep);

        progressBar = findViewById(R.id.core4Bar);
        progressTxt = findViewById(R.id.core4Text);


        goodnessIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Goodness In Checked", Toast.LENGTH_SHORT).show();
                toggleProgress(25);
                if (buttonClicked[0] && toggleProgress(currentProgress) == 25 ) {
                    toggleProgress(0);
                }

            }
        });

        hydration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Hydration Checked", Toast.LENGTH_SHORT).show();
                toggleProgress(25);

            }
        });

        movement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Movement Checked", Toast.LENGTH_SHORT).show();
                if (buttonClicked[1]) {
                    toggleProgress(25);
                } else {
                    toggleProgress(-25);
                }

            }
        });

        sleep.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean buttonClickTrue = buttonClicked[1];
                boolean buttonClickFalse = buttonClicked[0];
                Toast.makeText(getContext(), "Sleep Checked", Toast.LENGTH_SHORT).show();
                if (buttonClickTrue) {
                    toggleProgress(25);
                } else if (buttonClickFalse){
                    toggleProgress(-25);
                }

            }
        });

//        goodnessIn.setOnClickListener(v -> toggleProgress(25));
//        hydration.setOnClickListener(v -> toggleProgress(25));
//        movement.setOnClickListener(v -> toggleProgress(25));
//        sleep.setOnClickListener(v -> toggleProgress(25));
    }

    private int toggleProgress(int progress) {
        if (currentProgress + progress >=0 && currentProgress + progress <= 100) {
            currentProgress += progress;
            setProgressBar(currentProgress);
            setProgressTxt(String.valueOf(currentProgress));
        }

        return progress;

    }

    public void setProgressTxt(String text) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, Integer.parseInt(text));
        valueAnimator.setDuration(1700);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                progressTxt.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }

    public void setProgressBar(int progress){
        ObjectAnimator.ofInt(progressBar, "progress",progress)
                .setDuration(1700)
                .start();
    }

    public void setProgressAndText(String progress) {
        setProgressTxt(progress);
        setProgressBar(Integer.parseInt(progress));
    }
}
