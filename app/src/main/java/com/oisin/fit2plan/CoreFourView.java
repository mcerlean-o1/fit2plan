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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoreFourView extends FrameLayout {

    ImageButton goodnessIn, hydration, movement, sleep,
            goodnessInToggled, hydrationToggled, movementToggled, sleepToggled;
    ProgressBar progressBar;
    TextView progressTxt;


    private final boolean[] isToggled = new boolean[4];
    private int currentProgress = 0;

    public CoreFourView(@NonNull Context context) {
        super(context);
        init();
    }

    public CoreFourView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CoreFourView);

        int progress = typedArray.getInteger(R.styleable.CoreFourView_progressAndText,0);
        setProgressAndText(String.valueOf(progress));
        typedArray.recycle();
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
        goodnessInToggled = findViewById(R.id.goodness_in_toggled);
        hydrationToggled = findViewById(R.id.hydration_toggled);
        movementToggled = findViewById(R.id.fitness_toggled);
        sleepToggled = findViewById(R.id.sleep_toggled);


        progressBar = findViewById(R.id.core4Bar);
        progressTxt = findViewById(R.id.core4Text);


        setUpButtonListeners(goodnessIn, goodnessInToggled, 0);
        setUpButtonListeners(hydration, hydrationToggled, 1);
        setUpButtonListeners(movement, movementToggled, 2);
        setUpButtonListeners(sleep, sleepToggled, 3);
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

    private void setUpButtonListeners(ImageButton button, ImageButton buttonClicked, int index) {

        button.setOnClickListener(v -> {
            toggleProgress(25);
            updateButtonToggled(button, buttonClicked, index);
        });

        buttonClicked.setOnClickListener(v -> {
            toggleProgress(-25);
            updateButtonToggled(button, buttonClicked, index);
        });
    }

    private void updateButtonToggled(ImageButton button, ImageButton buttonToggled, int index) {
        if (isToggled[index]) {
            buttonToggled.setVisibility(GONE);
            button.setVisibility(VISIBLE);
        } else {
            button.setVisibility(GONE);
            buttonToggled.setVisibility(VISIBLE);
        }
        isToggled[index] = !isToggled[index];

    }
}
