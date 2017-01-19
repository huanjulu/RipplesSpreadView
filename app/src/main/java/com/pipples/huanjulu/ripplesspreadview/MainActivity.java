package com.pipples.huanjulu.ripplesspreadview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    AnimatorSet animatorSet;
    ImageView heartIma;
    RipplesSpreadView ripplesSpreadView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ripplesSpreadView = (RipplesSpreadView) findViewById(R.id.ripple_view);
        heartIma = (ImageView) findViewById(R.id.heart_image);
        onInitAnimatorSet();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onStartWave();

            }
        });
    }


    private void onStartWave() {
        animatorSet.start();
        ripplesSpreadView.start();
    }


    private void onInitAnimatorSet() {
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(200);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(heartIma, heartIma.SCALE_Y, 1f, 1.5f, 1f), ObjectAnimator.ofFloat(heartIma, heartIma.SCALE_X, 1f, 1.5f, 1f)
        );
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        ripplesSpreadView.setDuration(1000);
        ripplesSpreadView.setStyle(Paint.Style.FILL);
        ripplesSpreadView.setColor(Color.parseColor("#ff0000"));
        ripplesSpreadView.setInterpolator(new LinearOutSlowInInterpolator());

    }


}
