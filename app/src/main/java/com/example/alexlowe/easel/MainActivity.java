package com.example.alexlowe.easel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.drawing) DrawingView drawingView;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_fine) FloatingActionButton fabFine;
    @BindView(R.id.fab_superfine) FloatingActionButton fabSuperfine;
    @BindView(R.id.fab_small) FloatingActionButton fabSmall;
    @BindView(R.id.fab_medium) FloatingActionButton fabMedium;
    @BindView(R.id.fab_large) FloatingActionButton fabLarge;

    private Animation fabOpen, fabClose;

    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
    }


    @OnClick({R.id.fab, R.id.fab_large, R.id.fab_medium, R.id.fab_small, R.id.fab_fine, R.id.fab_superfine})
    public void pressFab(View view){
        float superfineBrush = getResources().getInteger(R.integer.superfine_size);
        float fineBrush = getResources().getInteger(R.integer.fine_size);
        float mediumBrush = getResources().getInteger(R.integer.medium_size);
        float largeBrush = getResources().getInteger(R.integer.large_size);
        float smallBrush = getResources().getInteger(R.integer.small_size);

        switch (view.getId()){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab_superfine:
                clickBrush(superfineBrush);
                break;
            case R.id.fab_fine:
                clickBrush(fineBrush);
                break;
            case R.id.fab_small:
                clickBrush(smallBrush);
                break;
            case R.id.fab_medium:
                clickBrush(mediumBrush);
                break;
            case R.id.fab_large:
                clickBrush(largeBrush);
                break;
        }
    }

    public void animateFAB() {
        if (isFabOpen) {

            fabSuperfine.startAnimation(fabClose);
            fabFine.startAnimation(fabClose);
            fabSmall.startAnimation(fabClose);
            fabMedium.startAnimation(fabClose);
            fabLarge.startAnimation(fabClose);
            fabSuperfine.setClickable(false);
            fabFine.setClickable(false);
            fabSmall.setClickable(false);
            fabMedium.setClickable(false);
            fabLarge.setClickable(false);
            isFabOpen = false;
        } else {

            fabSuperfine.startAnimation(fabOpen);
            fabFine.startAnimation(fabOpen);
            fabSmall.startAnimation(fabOpen);
            fabMedium.startAnimation(fabOpen);
            fabLarge.startAnimation(fabOpen);
            fabSuperfine.setClickable(true);
            fabFine.setClickable(true);
            fabSmall.setClickable(true);
            fabMedium.setClickable(true);
            fabLarge.setClickable(true);
            isFabOpen = true;
        }
    }

    private void clickBrush(float brushSize){
        drawingView.setBrushSize(brushSize);
        animateFAB();
    }
}
